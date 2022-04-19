package com.cleverpy.moviesAPI.dto.productionCountry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Dto with a list of Production Countries DTOs used in methods with pagination
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductionCountriesPageDto {

    private Integer totalPages;
    private Long totalElements;
    private List<ProductionCountryDto> countries;

}
