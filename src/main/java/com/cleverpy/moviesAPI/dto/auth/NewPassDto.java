package com.cleverpy.moviesAPI.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * DTO with the required data for setting a new password for the user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPassDto {

    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "New Pass is mandatory")
    private String newPass;

    @NotBlank(message = "Validation Code is mandatory")
    private Integer validationCode;

}
