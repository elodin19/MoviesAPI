package com.cleverpy.moviesAPI.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * DTO with the required data for the activation of a new user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivateUserDto {

    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Activation Code is mandatory")
    private Integer activationCode;

}
