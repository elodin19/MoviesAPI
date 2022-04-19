package com.cleverpy.moviesAPI.services.productionCompany;

import com.cleverpy.moviesAPI.dto.productionCompany.ProductionCompanyDto;
import com.cleverpy.moviesAPI.entities.Movie;
import com.cleverpy.moviesAPI.entities.ProductionCompany;
import com.cleverpy.moviesAPI.repositories.MovieRepository;
import com.cleverpy.moviesAPI.repositories.ProductionCompanyRepository;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the Production Company Service Implementation
 */
@Service
public class ProductionCompanyServiceImpl implements ProductionCompanyService {

    @Autowired
    private ProductionCompanyRepository companyRepository;

    @Autowired
    private MovieRepository movieRepository;

    public ProductionCompanyServiceImpl(ProductionCompanyRepository companyRepository, MovieRepository movieRepository) {
        this.companyRepository = companyRepository;
        this.movieRepository = movieRepository;
    }

    /**
     * Method to create a new Production Company
     * @param companyDto
     * @return ResponseEntity (ok: productionCompanyDto, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> create(ProductionCompanyDto companyDto) {

        //Tests if the company name already exists
        if (companyRepository.existsByName(companyDto.getName()))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The production company " + companyDto.getName() + " is already registered"));

        //Tests if the logo path already exists
        if (companyRepository.existsByLogoPath(companyDto.getLogoPath()))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The logo path " + companyDto.getLogoPath() + " is already registered"));

        //Creates and saves the new company
        ProductionCompany company = new ProductionCompany(null, companyDto.getName(), companyDto.getLogoPath(),
                companyDto.getOriginCountry(), null);
        companyRepository.save(company);

        return ResponseEntity.ok(company.getDto());
    }

    /**
     * Method to get the Production Company data
     * @param id
     * @return ResponseEntity (ok: productionCompanyDto, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> getById(Long id) {

        //Gets the company
        Optional<ProductionCompany> companyOpt = companyRepository.findById(id);

        return ResponseEntity.ok(companyOpt.get().getDto());
    }

    /**
     * Method to get all Production Companies
     * @return ResponseEntity (List<productionCompanyDto>, no content)
     */
    @Override
    public ResponseEntity<?> getAll() {

        List<ProductionCompany> companies = companyRepository.findAll();

        if (companies.size() == 0)
            return ResponseEntity.noContent().build();

        List<ProductionCompanyDto> companiesDto = new ArrayList<>();
        for (ProductionCompany company : companies) companiesDto.add(company.getDto());

        return ResponseEntity.ok(companiesDto);
    }

    /**
     * Method to get all the movies from a specific Production Company
     * @param id
     * @return ResponseEntity (ok: List<Movie>, no content)
     */
    @Override
    public ResponseEntity<?> getMovies(Long id) {

        //Gets the company
        Optional<ProductionCompany> companyOpt = companyRepository.findById(id);

        if (companyOpt.get().getMovies().size() == 0)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(companyOpt.get().getMovies());

    }

    /**
     * Method to update the Production Company
     * @param id
     * @param companyDto
     * @return ResponseEntity (ok: companyDto, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> update(Long id, ProductionCompanyDto companyDto) {

        //Gets the company
        Optional<ProductionCompany> companyOpt = companyRepository.findById(id);

        //Tests if the name or the logo path already exists
        if (companyRepository.existsByName(companyDto.getName()) && !companyOpt.get().getName().equalsIgnoreCase(companyDto.getName()))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The production company " + companyDto.getName() + " is already registered"));

        if (companyRepository.existsByLogoPath(companyDto.getLogoPath()) && !companyOpt.get().getLogoPath().equalsIgnoreCase(companyDto.getLogoPath()))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The logo path " + companyDto.getLogoPath() + " is already registered"));

        //Updates and saves the company
        companyOpt.get().setName(companyDto.getName());
        companyOpt.get().setLogoPath(companyDto.getLogoPath());
        companyOpt.get().setOriginCountry(companyDto.getOriginCountry());
        companyRepository.save(companyOpt.get());

        return ResponseEntity.ok(companyOpt.get().getDto());

    }

    /**
     * Method to delete a Production Company
     * If a movie is associated to only this Production Company, the movie is removed too
     * @param id
     * @return ResponseEntity(MessageResponse)
     */
    @Override
    public ResponseEntity<?> delete(Long id) {

        //Gets the company
        Optional<ProductionCompany> companyOpt = companyRepository.findById(id);

        //Check the movies to see if they need to be removed too
        for (Movie movie : companyOpt.get().getMovies()){
            for (ProductionCompany company : movie.getProductionCompanies()){
                if (company.equals(companyOpt.get())) {
                    movie.getProductionCompanies().remove(companyOpt.get());
                    if (movie.getProductionCompanies().size() < 1)
                        movieRepository.delete(movie);
                }
            }
        }

        //Delete the company
        companyRepository.delete(companyOpt.get());

        return ResponseEntity.ok(new MessageResponse("Production Company " + id + " deleted with success"));

    }
}
