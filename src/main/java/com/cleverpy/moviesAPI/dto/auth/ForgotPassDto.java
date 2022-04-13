package com.cleverpy.moviesAPI.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO with the required data for asking for a new password
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPassDto {

    private String email;
}
