package com.cleverpy.moviesAPI.controllers;

import com.cleverpy.moviesAPI.entities.User;
import com.cleverpy.moviesAPI.repositories.UserRepository;
import com.cleverpy.moviesAPI.security.jwt.JwtAuthEntryPoint;
import com.cleverpy.moviesAPI.security.jwt.JwtTokenUtil;
import com.cleverpy.moviesAPI.security.payload.LoginRequest;
import com.cleverpy.moviesAPI.security.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @MockBean
    private AuthenticationManager authManager;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private UserRepository repository;

    @MockBean
    private PasswordEncoder encoder;

    @MockBean
    private UserDetailsServiceImpl userDetails;

    @MockBean
    private JwtAuthEntryPoint entryPoint;

    @Autowired
    private MockMvc MockMvc;

    @Test
    void login() throws Exception {
        LoginRequest dto = new LoginRequest("admin", "admin");
        User user = new User(1L, "admin@gmail.com", "admin", encoder.encode("1234"), null);

        /*Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        String token = jwtTokenUtil.generateJwtToken(authentication);*/

        when(repository.findByUsername(dto.getUsername())).thenReturn(Optional.of(user));

        MockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"admin\", \"password\": \"admin\"}"))
                        .andExpect(status().isOk());



    }

    @Test
    void shouldNotLoginBadCredential() throws Exception {
        LoginRequest dto = new LoginRequest("user", "user");
        User user = new User(1L, "admin@gmail.com", "admin", encoder.encode("1234"), null);

        when(repository.findByUsername(dto.getUsername())).thenThrow(NoSuchElementException.class);

        MockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"user\", \"password\": \"user\"}"))
                        .andExpect(status().isBadRequest());
    }
}