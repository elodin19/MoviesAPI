package com.cleverpy.moviesAPI.services.productionCountry;

import com.cleverpy.moviesAPI.dto.productionCountry.ProductionCountryDto;
import org.springframework.http.ResponseEntity;

/**
 * Production Country Service Interface
 */
public interface ProductionCountryService {

    ResponseEntity<?> create(ProductionCountryDto countryDto);
    ResponseEntity<?> getById(Long id);
    ResponseEntity<?> getAll();
    ResponseEntity<?> getMovies(Long id);
    ResponseEntity<?> update(Long id, ProductionCountryDto countryDto);
    ResponseEntity<?> delete(Long id);

}
