package com.cleverpy.moviesAPI.services.user;

import com.cleverpy.moviesAPI.dto.user.NewUserDto;
import com.cleverpy.moviesAPI.dto.user.UpdateUserDto;
import org.springframework.http.ResponseEntity;

/**
 * User service interface
 */
public interface UserService {

    ResponseEntity<?> createUser(NewUserDto newUser);
    ResponseEntity<?> getUser(Long id, String username);
    ResponseEntity<?> getAllUsers();
    ResponseEntity<?> updateUser(Long id, UpdateUserDto userDto, String username);
    ResponseEntity<?> deleteUser(Long id, String username);
}
