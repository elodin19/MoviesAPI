package com.cleverpy.moviesAPI.controllers;

import com.cleverpy.moviesAPI.dto.auth.ActivateUserDto;
import com.cleverpy.moviesAPI.dto.auth.ForgotPassDto;
import com.cleverpy.moviesAPI.dto.auth.NewPassDto;
import com.cleverpy.moviesAPI.entities.User;
import com.cleverpy.moviesAPI.repositories.UserRepository;
import com.cleverpy.moviesAPI.security.jwt.JwtTokenUtil;
import com.cleverpy.moviesAPI.security.payload.JwtResponse;
import com.cleverpy.moviesAPI.security.payload.LoginRequest;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import com.cleverpy.moviesAPI.services.auth.AuthService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Controller to manage the authentications
 * When the authentication is ok it sends a JWT token as answer
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final AuthService authService;

    public AuthController(AuthenticationManager authManager, JwtTokenUtil jwtTokenUtil, UserRepository userRepository,
                          AuthService authService) {
        this.authManager = authManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    /**
     * Method to login
     * @param loginRequest (username and password)
     * @return ResponseEntity (ok: jwt token, bad request: messageResponse)
     */
    @PostMapping("/login")
    @ApiOperation("Login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){

        try {
            Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());

        } catch (NoSuchElementException e){

            System.err.println("Error: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The user " + loginRequest.getUsername() + " doesn't exist"));
        }

        //Authentication
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateJwtToken(authentication);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    /**
     * Method to activate the user
     * * The user won't be able to log in before activating the account!
     *
     * @param activateUser
     * @return ResponseEntity
     */
    @PostMapping("/activate-user")
    @ApiOperation("Activates the new user")
    public ResponseEntity<?> activateUser(@RequestBody ActivateUserDto activateUser){

        //Validates the DTO
        if (activateUser.getUsername() == null || activateUser.getActivationCode() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Missing parameters"));

        //Validates de user
        Optional<User> userOpt = userRepository.findByUsername(activateUser.getUsername());

        if (!userOpt.isPresent())
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The user" + activateUser.getUsername() + " doesn't exist"));

        return authService.activateUser(userOpt.get(), activateUser.getActivationCode());
    }

    /**
     * Method to ask for a new password
     * @param forgotPass
     * @return ResponseEntity (MessageReponse)
     */
    @PostMapping("/forgot-pass")
    @ApiOperation("Asks for a new password")
    public ResponseEntity<?> forgotPass(@RequestBody ForgotPassDto forgotPass){

        //Validates the DTO
        if (forgotPass.getEmail() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Missing parameters"));

        //Validates the user
        Optional<User> userOpt = userRepository.findByEmail(forgotPass.getEmail());

        if (!userOpt.isPresent())
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The email " + forgotPass.getEmail() + " isn't registered"));

        return authService.forgotPass(userOpt.get());
    }

    /**
     * Method that sets a new password to the user
     * @param newPass
     * @return ResponseEntity (MessageReponse)
     */
    @PostMapping("/save-pass")
    @ApiOperation("Save new password")
    public ResponseEntity<?> setNewPass(@RequestBody NewPassDto newPass){

        //Validates the DTO
        if (newPass.getUsername() == null ||
                newPass.getNewPass() == null ||
                newPass.getValidationCode() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Missing parameters"));

        //Validates de user
        Optional<User> userOpt = userRepository.findByUsername(newPass.getUsername());

        if (!userOpt.isPresent())
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The user" + newPass.getUsername() + " doesn't exist"));

        return authService.setNewPass(userOpt.get(), newPass.getNewPass(), newPass.getValidationCode());
    }
}
