package com.cleverpy.moviesAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Dto with the data needed for the CRUD operations of Spoken Language
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpokenLanguageDto {

    @NotBlank(message = "English Name is mandatory")
    private String english_name;

    @NotBlank(message = "Iso is mandatory")
    private String iso;

    @NotBlank(message = "Name is mandatory")
    private String name;
}
