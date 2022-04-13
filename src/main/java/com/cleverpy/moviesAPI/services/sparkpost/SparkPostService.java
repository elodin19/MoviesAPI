package com.cleverpy.moviesAPI.services.sparkpost;

import com.cleverpy.moviesAPI.entities.User;
import com.sparkpost.exception.SparkPostException;
import org.springframework.http.ResponseEntity;

/**
 * Interface for the SparkPost methods
 */
public interface SparkPostService {

    ResponseEntity<?> sendActivationMessage(User user) throws SparkPostException;
    void sendWelcomeMessage(User user) throws SparkPostException;
    ResponseEntity<?> sendForgotPassMessage(User user) throws SparkPostException;
    void sendChangedPassMessage(User user) throws SparkPostException;
    void sendUserUpdatedMessage(User user) throws SparkPostException;
    void sendUserRemovedMessage(User user) throws SparkPostException;
    int randomNumber();

}
