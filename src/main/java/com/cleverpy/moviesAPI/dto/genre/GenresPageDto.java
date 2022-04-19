package com.cleverpy.moviesAPI.dto.genre;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Dto with a list of Genre DTOs used in methods with pagination
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenresPageDto {

    private Integer totalPages;
    private Long totalElements;
    private List<GenreDto> genres;

}
