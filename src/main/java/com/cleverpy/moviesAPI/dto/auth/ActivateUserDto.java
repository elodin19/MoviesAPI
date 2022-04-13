package com.cleverpy.moviesAPI.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO with the required data for the activation of a new user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivateUserDto {

    private String username;
    private Integer activationCode;

}
