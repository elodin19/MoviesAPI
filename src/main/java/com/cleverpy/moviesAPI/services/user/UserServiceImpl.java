package com.cleverpy.moviesAPI.services.user;

import com.cleverpy.moviesAPI.dto.UserDto;
import com.cleverpy.moviesAPI.entities.Role;
import com.cleverpy.moviesAPI.entities.User;
import com.cleverpy.moviesAPI.repositories.RoleRepository;
import com.cleverpy.moviesAPI.repositories.UserRepository;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    /**
     * Method to create a new user
     * @param newUser (NewUserRequest)
     * @return ResponseEntity (ok: User, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> createUser(UserDto newUser) {

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

        return ResponseEntity.ok().body(user);
    }

    /**
     * Method to get user
     * @param id
     * @return ResponseEntity (ok: User, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> getById(Long id, String username) {

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
                                + id));
        }

        return ResponseEntity.ok(userOpt.get());
    }

    /**
     * Method to get all users
     * @return ResponseEntity (Page<User>, no content)
     */
    @Override
    public ResponseEntity<?> getAllUsers(Integer pageNumber) {

        Pageable page = PageRequest.of(pageNumber, 10);
        Page<User> users = userRepository.findAll(page);

        if (users == null)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(users);
    }

    /**
     * Method to update User (only username, email, isActivated)
     * Sends an alert email after the update
     * @param id
     * @param userDto
     * @return ResponseEntity (ok: User, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> updateUser(Long id, UserDto userDto, String username) {

        //Get Users
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

        //Starts updating
        userOpt.get().setUsername(userDto.getUsername());
        userOpt.get().setEmail(userDto.getEmail());
        userOpt.get().setPassword(encoder.encode(userDto.getPassword()));

        userRepository.save(userOpt.get());

        return ResponseEntity.ok(userOpt.get());
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
        return ResponseEntity.ok().body(new MessageResponse("User " + id + " deleted with success"));
    }
}
