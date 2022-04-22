package com.cleverpy.moviesAPI.services.user;

import com.cleverpy.moviesAPI.dto.UserDto;
import org.springframework.http.ResponseEntity;

/**
 * User service interface
 */
public interface UserService {

    ResponseEntity<?> createUser(UserDto newUser);
    ResponseEntity<?> getById(Long id, String username);
    ResponseEntity<?> getAllUsers(Integer pageNumber);
    ResponseEntity<?> updateUser(Long id, UserDto userDto, String username);
    ResponseEntity<?> deleteUser(Long id, String username);
}
