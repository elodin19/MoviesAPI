package com.cleverpy.moviesAPI.controllers;

import com.cleverpy.moviesAPI.dto.ProductionCountryDto;
import com.cleverpy.moviesAPI.repositories.ProductionCountryRepository;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import com.cleverpy.moviesAPI.services.productionCountry.ProductionCountryServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller to manage the CRUD operations of the Production Countries
 * Requires authentication
 */
@RestController
@RequestMapping("/api/country")
public class ProductionCountryController {

    private final ProductionCountryServiceImpl countryService;
    private final ProductionCountryRepository countryRepository;

    public ProductionCountryController(ProductionCountryServiceImpl countryService, ProductionCountryRepository countryRepository) {
        this.countryService = countryService;
        this.countryRepository = countryRepository;
    }

    /**
     * Endpoint to create a new Production Country
     * Name and iso are mandatory and must be unique
     * @param countryDto
     * @return ResponseEntity (ok: ProductionCountry, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/")
    @ApiOperation("Creates new Production Country. Authentication required (ADMIN). Name and iso are mandatory and must be unique")
    public ResponseEntity<?> createCountry(@Valid @RequestBody ProductionCountryDto countryDto){

        if (countryDto.getIso().length() != 2)
            return ResponseEntity.badRequest().body(new MessageResponse("Origin Country must have a length of 2"));

        return countryService.create(countryDto);
    }

    /**
     * Endpoint to get a Production Country by id
     * @param country_id
     * @return ResponseEntity (ok: ProductionCountry, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{country_id}")
    @ApiOperation("Gets a Production Country by id. Authentication required (USER)")
    public ResponseEntity<?> getById(@PathVariable Long country_id){

        //Validates the id
        if (!countryRepository.existsById(country_id))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        return countryService.getById(country_id);
    }

    /**
     * Endpoint to get all Production Countries
     * @return ResponseEntity (ok: Page<ProductionCountry>, no content)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/page/{page_number}")
    @ApiOperation("Gets all Production Countries. Authentication required (USER)")
    public ResponseEntity<?> getAll(@PathVariable Integer page_number){
        return countryService.getAll(page_number);
    }

    /**
     * Endpoint to update the Production Country
     * Name and iso are mandatory and must be unique
     * @param country_id
     * @param countryDto
     * @return ResponseEntity (ok: ProductionCountry, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{country_id}")
    @ApiOperation("Updates the Production Country. Authentication required (ADMIN). Name and iso are mandatory and must be unique")
    public ResponseEntity<?> update(@PathVariable Long country_id, @Valid @RequestBody ProductionCountryDto countryDto){

        //Validates id
        if (!countryRepository.existsById(country_id))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        return countryService.update(country_id, countryDto);
    }

    /**
     * Endpoint to delete the Production Country
     * @param country_id
     * @return ResponseEntity (messageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{country_id}")
    @ApiOperation("Deletes the Production Country. Authentication required (ADMIN)")
    public ResponseEntity<?> delete(@PathVariable Long country_id){

        //Validates id
        if (!countryRepository.existsById(country_id))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        return countryService.delete(country_id);
    }
}
