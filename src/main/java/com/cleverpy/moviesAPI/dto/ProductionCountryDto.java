package com.cleverpy.moviesAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Dto with the data needed for the CRUD operations of Production Country
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductionCountryDto {

    @NotBlank(message = "iso is mandatory")
    private String iso;

    @NotBlank(message = "Name is mandatory")
    private String name;
}
