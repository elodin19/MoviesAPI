package com.cleverpy.moviesAPI.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO with the required data for the creation of a new user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDto {

    private String email;
    private String username;
    private String password;

}
