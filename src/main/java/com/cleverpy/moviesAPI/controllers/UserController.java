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

import javax.validation.Valid;

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
     * Email, username and password and mandatory
     * Email and username must be unique
     * @param newUser
     * @return ResponseEntity (ok: NewUserResponse, bad request: messageResponse)
     */
    @PostMapping("/new-user")
    @ApiOperation("Creates new user")
    public ResponseEntity<?> createUser(@Valid @RequestBody NewUserDto newUser){
        return userService.createUser(newUser);
    }

    //TODO: Refactor users - look for @Valid @NotBlank comments and pagenation

    /**
     * Gets the user data
     * @param id
     * @return ResponseEntity (ok: UserResponseDto, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}")
    @ApiOperation("Gets user data. Authentication required (USER)")
    public ResponseEntity<?> getById(@PathVariable Long id, @CurrentSecurityContext(expression="authentication?.name") String username){

        //Validates the id
        if (!userRepository.existsById(id))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The user id " + id + " doesn't exist!"));

        return userService.getById(id, username);
    }

    /**
     * Gets all users
     * @return ResponseEntity (ok: UsersPageDto, no content)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/page/{page_number}")
    @ApiOperation("Gets all users data. Authentication required (ADMIN)")
    public ResponseEntity<?> getAll(@PathVariable Integer page_number){
        return userService.getAllUsers(page_number);
    }

    /**
     * Updates user
     * Email and username are mandatory and must be unique
     * @param id
     * @param userDto
     * @return ReponseEntity (ok: UserResponseDto, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/{id}")
    @ApiOperation("Updates user. Authentication required (USER)")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UpdateUserDto userDto,
                                    @CurrentSecurityContext(expression="authentication?.name") String username){

        //Validates the id
        if (!userRepository.existsById(id))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The user id " + id + " doesn't exist!"));

        return userService.updateUser(id, userDto, username);
    }

    /**
     * Removes user
     * @param id
     * @return ResponseEntity (messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{id}")
    @ApiOperation("Deletes user. Authentication required (USER)")
    public ResponseEntity<?> delete(@PathVariable Long id, @CurrentSecurityContext(expression="authentication?.name") String username){
        //Validates the id
        if (!userRepository.existsById(id))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The user id " + id + " doesn't exist!"));

        return userService.deleteUser(id, username);
    }
}
