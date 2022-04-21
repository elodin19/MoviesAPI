package com.cleverpy.moviesAPI.controllers;

import com.cleverpy.moviesAPI.entities.User;
import com.cleverpy.moviesAPI.repositories.UserRepository;
import com.cleverpy.moviesAPI.security.jwt.JwtTokenUtil;
import com.cleverpy.moviesAPI.security.payload.JwtResponse;
import com.cleverpy.moviesAPI.security.payload.LoginRequest;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
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

import javax.validation.Valid;
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

    public AuthController(AuthenticationManager authManager, JwtTokenUtil jwtTokenUtil, UserRepository userRepository) {
        this.authManager = authManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }

    /**
     * Method to login
     * @param loginRequest
     * @return ResponseEntity (ok: jwt token, bad request: messageResponse)
     */
    @PostMapping("/login")
    @ApiOperation("Login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){

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

}
