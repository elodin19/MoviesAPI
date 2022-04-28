package com.cleverpy.moviesAPI.controllers;

import com.cleverpy.moviesAPI.repositories.UserRepository;
import com.cleverpy.moviesAPI.security.jwt.JwtAuthEntryPoint;
import com.cleverpy.moviesAPI.security.jwt.JwtTokenUtil;
import com.cleverpy.moviesAPI.security.payload.LoginRequest;
import com.cleverpy.moviesAPI.security.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtAuthEntryPoint entryPoint;

    @Autowired
    private MockMvc MockMvc;

    @Test
    void login() throws Exception {
        LoginRequest dto = new LoginRequest("admin", "admin");
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        UserDetails userDetails = new User("admin", "admin", authorities);
        com.cleverpy.moviesAPI.entities.User user = new com.cleverpy.moviesAPI.entities.User(1L, "admin@gmail.com", "admin", "admin", null);
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        String jwtToken = jwtTokenUtil.generateJwtToken(authentication);

        when(repository.findByUsername(dto.getUsername())).thenReturn(Optional.of(user));
        when(authManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()))).thenReturn(authentication);
        when(userDetailsService.loadUserByUsername(eq("admin"))).thenReturn(userDetails);

        MockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                        .andExpect(status().isOk())
                .       andExpect(jsonPath("$.token", Matchers.notNullValue()));

        //FIXME: Still can't generate a token
    }

    @Test
    void shouldNotLoginBadCredential() throws Exception {
        LoginRequest dto = new LoginRequest("user", "user");

        when(repository.findByUsername(dto.getUsername())).thenThrow(NoSuchElementException.class);

        MockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                        .andExpect(status().isBadRequest());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}