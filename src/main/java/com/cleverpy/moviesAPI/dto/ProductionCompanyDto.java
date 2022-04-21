package com.cleverpy.moviesAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Dto with the data needed for the CRUD operations of Production Company
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductionCompanyDto {

    public Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Logo Path is mandatory")
    private String logo_path;

    @NotBlank(message = "Origin Country is mandatory")
    private String origin_country;

}
