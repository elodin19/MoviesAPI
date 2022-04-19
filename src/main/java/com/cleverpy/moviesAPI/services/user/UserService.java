package com.cleverpy.moviesAPI.services.user;

import com.cleverpy.moviesAPI.dto.user.NewUserDto;
import com.cleverpy.moviesAPI.dto.user.UpdateUserDto;
import org.springframework.http.ResponseEntity;

/**
 * User service interface
 */
public interface UserService {

    ResponseEntity<?> createUser(NewUserDto newUser);
    ResponseEntity<?> getById(Long id, String username);
    ResponseEntity<?> getAllUsers(Integer pageNumber);
    ResponseEntity<?> updateUser(Long id, UpdateUserDto userDto, String username);
    ResponseEntity<?> deleteUser(Long id, String username);
}
