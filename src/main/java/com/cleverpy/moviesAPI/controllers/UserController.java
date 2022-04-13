package com.cleverpy.moviesAPI.controllers;

import com.cleverpy.moviesAPI.dto.user.NewUserDto;
import com.cleverpy.moviesAPI.dto.user.UpdateUserDto;
import com.cleverpy.moviesAPI.repositories.UserRepository;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import com.cleverpy.moviesAPI.services.user.UserServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

/**
 *  Controller to manage the User CRUD
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    private final UserServiceImpl userService;

    public UserController(UserRepository userRepository, UserServiceImpl userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * Method to create a new user
     * @param newUser
     * @return ResponseEntity (ok: NewUserResponse, bad request: messageResponse)
     */
    @PostMapping("/new-user")
    @ApiOperation("Creates new user")
    public ResponseEntity<?> newUser(@RequestBody NewUserDto newUser){

        //Validates the DTO
        if (newUser.getUsername() != null &&
                newUser.getEmail() != null &&
                newUser.getPassword() != null)
            return userService.createUser(newUser);

        return ResponseEntity.badRequest()
                .body(new MessageResponse("Missing parameters"));

    }

    /**
     * Gets the user data
     * @param id
     * @return ResponseEntity (ok: user, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}")
    @ApiOperation("Gets user data. Authentication required (USER)")
    public ResponseEntity<?> getUser(@PathVariable Long id){

        //Validates the id
        if (!userRepository.existsById(id))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The user id " + id + " doesn't exist!"));

        return userService.getUser(id);
    }

    /**
     * Gets all users
     * @return users
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all")
    @ApiOperation("Gets all users data. Authentication required (ADMIN)")
    public ResponseEntity<?> getAllUsers(){
        return userService.getAllUsers();
    }

    /**
     * Updates user (only username, email and isValidated)
     * @param id
     * @param userDto
     * @return ReponseEntity (with the User or an error message)
     */
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/{id}")
    @ApiOperation("Updates user. Authentication required (USER)")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UpdateUserDto userDto,
                                        @CurrentSecurityContext(expression="authentication?.name") String username){

        //Validates the DTO
        if (userDto.getUsername() == null || userDto.getEmail() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Missing parameters"));

        //Validates the id
        if (!userRepository.existsById(id))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The user id " + id + " doesn't exist!"));

        return userService.updateUser(id, userDto, username);
    }

    /**
     * Removes user
     * @param id
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{id}")
    @ApiOperation("Deletes user. Authentication required (USER)")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, @CurrentSecurityContext(expression="authentication?.name") String username){
        //Validates the id
        if (!userRepository.existsById(id))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The user id " + id + " doesn't exist!"));

        return userService.deleteUser(id, username);
    }
}
