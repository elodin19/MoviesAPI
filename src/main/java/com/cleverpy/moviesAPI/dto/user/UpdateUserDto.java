package com.cleverpy.moviesAPI.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO with the required data for the update of an user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    private String email;
    private String username;
    private String password;
    private Boolean isActivated;

}
