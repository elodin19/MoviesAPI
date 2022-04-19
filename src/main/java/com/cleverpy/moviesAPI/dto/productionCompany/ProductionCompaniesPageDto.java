package com.cleverpy.moviesAPI.dto.productionCompany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Dto with a list of Production Company Dtos used in methods with pagination
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductionCompaniesPageDto {

    private Integer totalPages;
    private Long totalElements;
    private List<ProductionCompanyDto> companies;

}
