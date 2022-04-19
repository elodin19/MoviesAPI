package com.cleverpy.moviesAPI.services.productionCountry;

import com.cleverpy.moviesAPI.dto.productionCountry.ProductionCountriesPageDto;
import com.cleverpy.moviesAPI.dto.productionCountry.ProductionCountryDto;
import com.cleverpy.moviesAPI.entities.Movie;
import com.cleverpy.moviesAPI.entities.ProductionCountry;
import com.cleverpy.moviesAPI.repositories.MovieRepository;
import com.cleverpy.moviesAPI.repositories.ProductionCountryRepository;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the Production Country Service Interface
 */
@Service
public class ProductionCountryServiceImpl implements ProductionCountryService{

    @Autowired
    private ProductionCountryRepository countryRepository;

    @Autowired
    private MovieRepository movieRepository;

    public ProductionCountryServiceImpl(ProductionCountryRepository countryRepository, MovieRepository movieRepository) {
        this.countryRepository = countryRepository;
        this.movieRepository = movieRepository;
    }

    /**
     * Method to create a new Production Country
     * Tests if name and logoPath aren't being used yet
     * @param countryDto
     * @return ResponseEntity (ok: productionCountryDto, bad request: messageResponse)
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
        ProductionCountry country = new ProductionCountry(null, countryDto.getIso(), countryDto.getName(), null);
        countryRepository.save(country);

        return ResponseEntity.ok(country.getDto());
    }

    /**
     * Method to get the Production Country data
     * @param id
     * @return ResponseEntity (ok: productionCountryDto, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> getById(Long id) {

        //Gets the country
        Optional<ProductionCountry> countryOpt = countryRepository.findById(id);

        return ResponseEntity.ok(countryOpt.get().getDto());
    }

    /**
     * Method to get all Production Countries
     * @return ResponseEntity (ProductionCountriesDto, no content)
     */
    @Override
    public ResponseEntity<?> getAll(Integer pageNumber) {

        Pageable page = PageRequest.of(pageNumber, 10);
        Page<ProductionCountry> countries = countryRepository.findAll(page);

        if (countries.toList().size() == 0)
            return ResponseEntity.noContent().build();

        List<ProductionCountryDto> countriesDto = new ArrayList<>();
        for (ProductionCountry country : countries) countriesDto.add(country.getDto());

        return ResponseEntity.ok(new ProductionCountriesPageDto(
                countries.getTotalPages(), countries.getTotalElements(), countriesDto
        ));
    }

    /**
     * Method to get all the movies from a specific Production Country
     * @param id
     * @return ResponseEntity (ok: List<Movie>, no content)
     */
    @Override
    public ResponseEntity<?> getMovies(Long id) {

        //Gets the country
        Optional<ProductionCountry> countryOpt = countryRepository.findById(id);

        if (countryOpt.get().getMovies().size() == 0)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(countryOpt.get().getMovies());
    }

    /**
     * Method to update the Production Country
     * Tests if name and logoPath aren't being used yet
     * @param id
     * @param countryDto
     * @return ResponseEntity (ok: countryDto, bad request: messageResponse)
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
                    .body(new MessageResponse("The iso " + countryDto.getIso() + " is already registered"));

        //Updates and saves the country
        countryOpt.get().setName(countryDto.getName());
        countryOpt.get().setIso(countryDto.getIso());
        countryRepository.save(countryOpt.get());

        return ResponseEntity.ok(countryOpt.get().getDto());
    }

    /**
     * Method to delete a Production Country
     * If a movie is associated to only this Production Country, the movie is removed too
     * @param id
     * @return ResponseEntity(MessageResponse)
     */
    @Override
    public ResponseEntity<?> delete(Long id) {

        //Gets the country
        Optional<ProductionCountry> countryOpt = countryRepository.findById(id);

        //Check the movies to see if they need to be removed too
        for (Movie movie : countryOpt.get().getMovies()){
            for (ProductionCountry country : movie.getProductionCountries()){
                if (country.equals(countryOpt.get())) {
                    movie.getProductionCountries().remove(countryOpt.get());
                    if(movie.getProductionCountries().size() < 1)
                        movieRepository.delete(movie);
                    else
                        movieRepository.save(movie);
                }
            }
        }

        countryRepository.delete(countryOpt.get());
        return ResponseEntity.ok(new MessageResponse("Production Country " + id + " deleted with success"));
    }
}
