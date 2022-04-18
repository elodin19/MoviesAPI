package com.cleverpy.moviesAPI.controllers;

import com.cleverpy.moviesAPI.entities.Role;
import com.cleverpy.moviesAPI.entities.User;
import com.cleverpy.moviesAPI.repositories.UserRepository;
import com.cleverpy.moviesAPI.security.payload.LoginRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class AuthControllerTest {

    UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);

    @Autowired
    AuthController authController;

    @BeforeAll
    void setUp(){

        Role roleAdmin = new Role(1L, "ADMIN");
        Role roleUser = new Role(2L, "USER");

        List<Role> rolesA = new ArrayList<Role>();
        rolesA.add(roleAdmin);
        rolesA.add(roleUser);

        List<Role> rolesB = new ArrayList<Role>();
        rolesB.add(roleUser);

        User userA = new User(1L, "joanoldaniel@gmail.com", "daniel", "pass1234", 999999, true, 000000,
                new Timestamp(System.currentTimeMillis()), rolesB);

        User userB = new User(2L, "admin@gmail.com", "admin", "pass1234", 999999, true, 000000,
                new Timestamp(System.currentTimeMillis()), rolesA);

        User userC = new User(3L, "newuser@gmail.com", "newuser", "pass1234", 999999, false, 000000,
                new Timestamp(System.currentTimeMillis()), rolesB);

        Mockito.when(authController.login(new LoginRequest(userA.getUsername(), userA.getPassword()))).thenReturn(ResponseEntity.ok().build());
    }

    @Test
    void login() {

    }

    @Test
    void activateUser() {
    }

    @Test
    void forgotPass() {
    }

    @Test
    void setNewPass() {
    }
}