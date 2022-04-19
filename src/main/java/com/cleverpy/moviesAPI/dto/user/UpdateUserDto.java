package com.cleverpy.moviesAPI.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * DTO with the required data for the update of an user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Username is mandatory")
    private String username;

    private String password;

    private Boolean isActivated;

}
