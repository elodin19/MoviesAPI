package com.cleverpy.moviesAPI.services.productionCompany;

import com.cleverpy.moviesAPI.dto.productionCompany.ProductionCompanyDto;
import org.springframework.http.ResponseEntity;

/**
 * Production Company Service Interface
 */
public interface ProductionCompanyService {

    ResponseEntity<?> create(ProductionCompanyDto companyDto);
    ResponseEntity<?> getById(Long id);
    ResponseEntity<?> getAll();
    ResponseEntity<?> getMovies(Long id);
    ResponseEntity<?> update(Long id, ProductionCompanyDto companyDto);
    ResponseEntity<?> delete(Long id);

}
