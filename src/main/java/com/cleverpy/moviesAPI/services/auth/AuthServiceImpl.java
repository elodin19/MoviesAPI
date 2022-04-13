package com.cleverpy.moviesAPI.services.auth;

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

import java.sql.Timestamp;

/**
 * Implementation of the Authentication Service Interface
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final PasswordEncoder encoder;

    @Autowired
    private final SparkPostServiceImpl sparkPost;

    private final Long EXPIRATION = 300000L; // ms equivalent to 5 minutes

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder,
                           SparkPostServiceImpl sparkPost) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.sparkPost = sparkPost;
    }

    /**
     * Method to validate a new user
     * @param user
     * @param activationCode int
     * @return ResponseEntity (messageResponse)
     */
    @Override
    public ResponseEntity<?> activateUser(User user, Integer activationCode) {

        if (user.getActivationCode() != null && activationCode.intValue() == user.getActivationCode().intValue()) {

            //Checks if more than 5 minutes has passed since the activation code was set up
            Long now = new Timestamp(System.currentTimeMillis()).getTime();

            if ( now - user.getTimeStamp().getTime() < EXPIRATION)
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("The activation code has expired"));

            user.setIsActivated(true);
            userRepository.save(user);
        } else {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The activation code is wrong"));
        }

        //Sends an email with a welcome message
        try {
            sparkPost.sendWelcomeMessage(user);
        } catch (SparkPostException e){
            System.out.println("Error: " + e.getMessage());
        }

        return ResponseEntity.ok()
                .body(new MessageResponse("Your account has been activated with success"));
    }

    /**
     * Method that sends an email to the user with a code to be validated when the user tries to create a new password
     * @param user
     * @return ResponseEntity (messageResponse)
     */
    @Override
    public ResponseEntity<?> forgotPass(User user) {

        try {
            sparkPost.sendForgotPassMessage(user);
        } catch (SparkPostException e){
            System.out.println("Error: " + e.getMessage());
        }

        return ResponseEntity.ok().body(new MessageResponse("Check your email account"));
    }

    /**
     * Method that change the password of the user
     * * The validation code must be correct for that to work!
     * @param user
     * @param newPass
     * @param validationCode
     * @return ResponseEntity (messageResponse)
     */
    @Override
    public ResponseEntity<?> setNewPass(User user, String newPass, Integer validationCode) {

        //Validates the code
        if (validationCode.intValue() != user.getValidationCode().intValue())
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Wrong validation code"));

        //Checks if more than 5 minutes has passed since the activation code was set up
        Long now = new Timestamp(System.currentTimeMillis()).getTime();

        if ( now - user.getTimeStamp().getTime() > EXPIRATION)
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The activation code has expired"));

        //Saves the new password
        user.setPassword(encoder.encode(newPass));
        userRepository.save(user);

        //Sends email confirming that the password has been changed
        try {
            sparkPost.sendChangedPassMessage(user);
        } catch (SparkPostException e){
            System.out.println("Error: " + e.getMessage());
        }

        return ResponseEntity.ok()
                .body(new MessageResponse("Password changed with success for " + user.getUsername()));
    }
}
