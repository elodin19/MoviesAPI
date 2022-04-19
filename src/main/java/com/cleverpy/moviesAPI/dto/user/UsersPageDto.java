package com.cleverpy.moviesAPI.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Dto with a list of User DTOs used in methods with pagination
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersPageDto {

    private Integer totalPages;
    private Long totalElements;
    private List<UserResponseDto> users;
}
