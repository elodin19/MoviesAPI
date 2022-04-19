package com.cleverpy.moviesAPI.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * DTO with the required data for the creation of a new user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDto {

    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    private String password;

}
