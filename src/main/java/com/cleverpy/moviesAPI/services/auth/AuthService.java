package com.cleverpy.moviesAPI.services.auth;

import com.cleverpy.moviesAPI.entities.User;
import org.springframework.http.ResponseEntity;

/**
 * Authentication service interface
 */
public interface AuthService {

    ResponseEntity<?> activateUser(User user, Integer activationCode);
    ResponseEntity<?> forgotPass(User user);
    ResponseEntity<?> setNewPass(User user, String newPass, Integer validationCode);

}
