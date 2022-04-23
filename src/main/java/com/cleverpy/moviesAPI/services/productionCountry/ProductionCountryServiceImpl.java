package com.cleverpy.moviesAPI.services.productionCountry;

import com.cleverpy.moviesAPI.dto.ProductionCountryDto;
import com.cleverpy.moviesAPI.entities.ProductionCountry;
import com.cleverpy.moviesAPI.repositories.ProductionCountryRepository;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the Production Country Service Interface
 */
@Service
public class ProductionCountryServiceImpl implements ProductionCountryService{

    @Autowired
    private ProductionCountryRepository countryRepository;

    public ProductionCountryServiceImpl(ProductionCountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    /**
     * Method to create a new Production Country
     * Tests if name and logoPath aren't being used yet
     * @param countryDto
     * @return ResponseEntity (ok: ProductionCountry, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> create(ProductionCountryDto countryDto) {

        //Tests if the country already exists
        if (countryRepository.existsByName(countryDto.getName()))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The production company " + countryDto.getName() + " is already registered"));

        //Tests if the ISO already exists
        if (countryRepository.existsByIso(countryDto.getIso()))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The iso_3166_1 " + countryDto.getIso() + " is already registered"));

        //Creates and saves the new country
        ProductionCountry country = new ProductionCountry(null, countryDto.getIso(), countryDto.getName());
        countryRepository.save(country);

        return ResponseEntity.ok(country);
    }

    /**
     * Method to get the Production Country data
     * @param id
     * @return ResponseEntity (ok: ProductionCountry, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> getById(Long id) {

        //Gets the country
        Optional<ProductionCountry> countryOpt = countryRepository.findById(id);

        return ResponseEntity.ok(countryOpt.get());
    }

    /**
     * Method to get all Production Countries
     * @return ResponseEntity (Page<ProductionCountries>, no content)
     */
    @Override
    public ResponseEntity<?> getAll(Integer pageNumber) {

        Pageable page = PageRequest.of(pageNumber, 10);
        Page<ProductionCountry> countries = countryRepository.findAll(page);

        if (countries == null)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(countries);
    }

    /**
     * Method to update the Production Country
     * Tests if name and logoPath aren't being used yet
     * @param id
     * @param countryDto
     * @return ResponseEntity (ok: ProductionCountry, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> update(Long id, ProductionCountryDto countryDto) {

        //Gets the country
        Optional<ProductionCountry> countryOpt = countryRepository.findById(id);

        //Tests if the name or the iso already exists
        if (countryRepository.existsByName(countryDto.getName()) && !countryOpt.get().getName().equalsIgnoreCase(countryDto.getName()))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The name " + countryDto.getName() + " is already registered"));

        if (countryRepository.existsByIso(countryDto.getIso()) && !countryOpt.get().getIso().equalsIgnoreCase(countryDto.getIso()))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The iso_3166_1 " + countryDto.getIso() + " is already registered"));

        //Updates and saves the country
        countryOpt.get().setName(countryDto.getName());
        countryOpt.get().setIso(countryDto.getIso());
        countryRepository.save(countryOpt.get());

        return ResponseEntity.ok(countryOpt.get());
    }

    /**
     * Method to delete a Production Country
     * @param id
     * @return ResponseEntity(MessageResponse)
     */
    @Override
    public ResponseEntity<?> delete(Long id) {

        //Gets the country
        Optional<ProductionCountry> countryOpt = countryRepository.findById(id);

        countryRepository.delete(countryOpt.get());
        return ResponseEntity.ok(new MessageResponse("Production Country " + id + " deleted with success"));
    }
}
