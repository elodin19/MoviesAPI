package com.cleverpy.moviesAPI.dto.genre;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto with the data required for the CRUD operations of Genre
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreDto {

    public String name;
}
