package com.cleverpy.moviesAPI.services.productionCountry;

import com.cleverpy.moviesAPI.dto.ProductionCountryDto;
import org.springframework.http.ResponseEntity;

/**
 * Production Country Service Interface
 */
public interface ProductionCountryService {

    ResponseEntity<?> create(ProductionCountryDto countryDto);
    ResponseEntity<?> getById(Long id);
    ResponseEntity<?> getAll(Integer pageNumber);
    ResponseEntity<?> update(Long id, ProductionCountryDto countryDto);
    ResponseEntity<?> delete(Long id);

}
