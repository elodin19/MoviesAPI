package com.cleverpy.moviesAPI.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO with the required data for the creation of a new user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String username;
    private String email;
    private Boolean isActivated;
    private List<String> roles;
    private String message;

}
