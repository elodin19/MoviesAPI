package com.cleverpy.moviesAPI.services.user;

import com.cleverpy.moviesAPI.dto.user.NewUserDto;
import com.cleverpy.moviesAPI.dto.user.UpdateUserDto;
import com.cleverpy.moviesAPI.dto.user.UserResponseDto;
import com.cleverpy.moviesAPI.entities.Role;
import com.cleverpy.moviesAPI.entities.User;
import com.cleverpy.moviesAPI.repositories.RoleRepository;
import com.cleverpy.moviesAPI.repositories.UserRepository;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import com.cleverpy.moviesAPI.services.sparkpost.SparkPostServiceImpl;
import com.sparkpost.exception.SparkPostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the User Service Interface
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final PasswordEncoder encoder;

    @Autowired
    private final SparkPostServiceImpl sparkPost;

    private final Long EXPIRATION = 30000L; // ms equivalent to 5 minutes

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder encoder, SparkPostServiceImpl sparkPost) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.sparkPost = sparkPost;
    }

    /**
     * Method to create a new user
     * @param newUser (NewUserRequest)
     * @return ResponseEntity (ok: userDto, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> createUser(NewUserDto newUser) {

        //Access the repository to check if the username isn't being used yet
        Optional<User> userOpt = userRepository.findByUsername(newUser.getUsername());

        if (userOpt.isPresent())
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The username " + newUser.getUsername() + " is already being used" ));

        //Access the repository to check if the email isn't being used yet
        userOpt = userRepository.findByEmail(newUser.getEmail());

        if (userOpt.isPresent())
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The email " + newUser.getEmail() + " is already being used" ));

        //If it gets here, the validations were ok, so we create a new user
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("USER").get());

        User user = new User(newUser.getEmail(), newUser.getUsername(),
                encoder.encode(newUser.getPassword()), roles);

        //Saves the user in the database
        userRepository.save(user);

        //Sends an email with the validation code;
        try {
            sparkPost.sendActivationMessage(user);
        } catch (SparkPostException e){
            System.err.println("Error: " + e.getMessage());
        }

        return ResponseEntity.ok().body(user.getDto("Check your email account"));
    }

    /**
     * Method to get user
     * @param id
     * @return ResponseEntity (ok: UserResponseDto, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> getUser(Long id) {

        Optional<User> userOpt = userRepository.findById(id);
        return ResponseEntity.ok(userOpt.get().getDto(" "));
    }

    /**
     * Method to get all users
     * @return List with UserResponseDto
     */
    @Override
    public ResponseEntity<?> getAllUsers() {

        //Gets all users
        List<User> users = userRepository.findAll();
        List<UserResponseDto> response = new ArrayList<UserResponseDto>();
        for (User user : users) response.add(user.getDto(" "));

        return ResponseEntity.ok(response);
    }

    /**
     * Method to update User (only username, email, isActivated)
     * Sends an alert email after the update
     * @param id
     * @param userDto
     * @return ResponseEntity (ok: User, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> updateUser(Long id, UpdateUserDto userDto, String username) {

        //Gets User
        Optional<User> userOpt = userRepository.findById(id);
        Optional<User> userConnecting = userRepository.findByUsername(username);

        //Tests if the user is the owner of the credentials or if it's admin
        if (!userConnecting.get().getUsername().equalsIgnoreCase(username)){

            boolean isAdmin = false;

            for (Role role  : userOpt.get().getRoles()){
                if (role.getName().equalsIgnoreCase("ADMIN")) isAdmin = true;
            }

            if (!isAdmin)
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("The user " + username + " is not allowed to update the user "
                                + userDto.getUsername()));
        }

        //Validates username
        if (userOpt.get().getUsername() != userDto.getUsername() &&
                userRepository.existsByUsername(userDto.getUsername()))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("This username is already being used" ));

        //Validates email
        if (userOpt.get().getEmail() != userDto.getEmail() &&
                userRepository.existsByEmail(userDto.getEmail())){
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("This email is already being used" ));
        }

        //If the email is different from the original, sets isActivated to false and sends a new activation code
        else if (userOpt.get().getEmail() != userDto.getEmail()) {

            //Starts updating
            userOpt.get().setUsername(userDto.getUsername());
            userOpt.get().setEmail(userDto.getEmail());
            userOpt.get().setIsActivated(false);
            userRepository.save(userOpt.get());

            //Sends an email to the user
            try {
                sparkPost.sendActivationMessage(userOpt.get());
            } catch (Exception e){
                System.out.println("Error :" + e.getMessage());
            }

            return ResponseEntity.ok(userOpt.get()
                    .getDto("Since your email address has changed, check you email account for the new activation code"));
        }

        //Starts updating
        userOpt.get().setUsername(userDto.getUsername());
        userOpt.get().setEmail(userDto.getEmail());

        if (userDto.getIsActivated() != null)
            userOpt.get().setIsActivated(userDto.getIsActivated());

        userRepository.save(userOpt.get());

        //Sends an email to the user
        try {
            sparkPost.sendUserUpdatedMessage(userOpt.get());
        } catch (Exception e){
            System.out.println("Error :" + e.getMessage());
        }

        return ResponseEntity.ok(userOpt.get().getDto("Your account has been updated"));
    }

    /**
     * Method to delete the user
     * Sends a goodbye email
     * @param id
     * @return ResponseEntity (messageResponse)
     */
    @Override
    public ResponseEntity<?> deleteUser(Long id, String username) {

        //Gets the user
        Optional<User> userOpt = userRepository.findById(id);
        Optional<User> userConnecting = userRepository.findByUsername(username);

        //Only the own user or an admin can remove the user
        if (!userOpt.get().getUsername().equalsIgnoreCase(username)){

            boolean isAdmin = false;

            for (Role role  : userConnecting.get().getRoles()){
                if (role.getName().equalsIgnoreCase("ADMIN")) isAdmin = true;
            }

            if (!isAdmin)
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("The user " + username + " is not allowed to delete the user " +
                                userOpt.get().getUsername()));
        }

        userRepository.delete(userOpt.get());

        //Sends an email to the user
        try {
            sparkPost.sendUserRemovedMessage(userOpt.get());
        } catch (Exception e){
            System.out.println("Error :" + e.getMessage());
        }

        return ResponseEntity.ok().body(new MessageResponse("User " + id + " deleted with success"));
    }
}
