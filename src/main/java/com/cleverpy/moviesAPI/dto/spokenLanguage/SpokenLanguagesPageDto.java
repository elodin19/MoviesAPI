package com.cleverpy.moviesAPI.dto.spokenLanguage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Dto with a list of Spoken Languages DTOs used in methods with pagination
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpokenLanguagesPageDto {

    private Integer totalPages;
    private Long totalElements;
    private List<SpokenLanguageDto> languages;

}
