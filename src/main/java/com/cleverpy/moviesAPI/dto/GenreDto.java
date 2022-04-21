package com.cleverpy.moviesAPI.dto;

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

    public Long id;

    @NotBlank(message = "Name is mandatory")
    public String name;
}
