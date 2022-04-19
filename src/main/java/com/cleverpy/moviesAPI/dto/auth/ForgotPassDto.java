package com.cleverpy.moviesAPI.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * DTO with the required data for asking for a new password
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPassDto {

    @NotBlank(message = "Email is mandatory")
    private String email;
}
