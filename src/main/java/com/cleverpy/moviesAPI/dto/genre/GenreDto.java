package com.cleverpy.moviesAPI.dto.genre;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Dto with the data required for the CRUD operations of Genre
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreDto {

    @NotBlank(message = "Name is mandatory")
    public String name;
}
