package com.cleverpy.moviesAPI.security.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto used to return the JWT token
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private String token;

}
