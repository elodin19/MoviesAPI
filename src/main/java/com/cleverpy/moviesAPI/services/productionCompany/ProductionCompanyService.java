package com.cleverpy.moviesAPI.services.productionCompany;

import com.cleverpy.moviesAPI.dto.ProductionCompanyDto;
import org.springframework.http.ResponseEntity;

/**
 * Production Company Service Interface
 */
public interface ProductionCompanyService {

    ResponseEntity<?> create(ProductionCompanyDto companyDto);
    ResponseEntity<?> getById(Long id);
    ResponseEntity<?> getAll(Integer pageNumber);
    ResponseEntity<?> update(Long id, ProductionCompanyDto companyDto);
    ResponseEntity<?> delete(Long id);

}
