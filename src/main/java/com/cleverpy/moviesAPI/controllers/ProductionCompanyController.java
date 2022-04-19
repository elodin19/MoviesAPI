package com.cleverpy.moviesAPI.controllers;

import com.cleverpy.moviesAPI.dto.productionCompany.ProductionCompanyDto;
import com.cleverpy.moviesAPI.repositories.ProductionCompanyRepository;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import com.cleverpy.moviesAPI.services.productionCompany.ProductionCompanyServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller to manage the CRUD operations of the ProductionCompanies
 * Requires authentication
 */
@RestController
@RequestMapping("/api/company")
public class ProductionCompanyController {

    private final ProductionCompanyServiceImpl companyService;
    private final ProductionCompanyRepository companyRepository;

    public ProductionCompanyController(ProductionCompanyServiceImpl companyService, ProductionCompanyRepository companyRepository) {
        this.companyService = companyService;
        this.companyRepository = companyRepository;
    }

    /**
     * Endpoint to create a new Production Company
     * Name, logoPath and OriginCountry are mandatory
     * Name and logoPath must be unique
     * @param companyDto
     * @return ResponseEntity (ok: ProductionCompanyDto, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/")
    @ApiOperation("Creates new Production Company. Authentication required (ADMIN). \nName, logoPath and OriginCountry are mandatory. " +
            "Name and logoPath must be unique")
    public ResponseEntity<?> createCompany(@Valid @RequestBody ProductionCompanyDto companyDto){

        if (companyDto.getOriginCountry().length() != 2)
            return ResponseEntity.badRequest().body(new MessageResponse("Origin Country must have a length of 2"));

        return companyService.create(companyDto);
    }

    /**
     * Endpoint to get a Production Company by id
     * @param company_id
     * @return ResponseEntity (ok: ProductionCompanyDto, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{company_id}")
    @ApiOperation("Gets a Production Company by id. Authentication required (USER)")
    public ResponseEntity<?> getById(@PathVariable Long company_id){

        //Validates the id
        if (!companyRepository.existsById(company_id))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        return companyService.getById(company_id);
    }

    /**
     * Endpoint to get all Production Companies
     * @return ResponseEntity (ok: ProductionCompaniesPageDto, no content)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/page/{page_number}")
    @ApiOperation("Gets all Production Companies. Authentication required (USER)")
    public ResponseEntity<?> getAll(@PathVariable Integer page_number){
        return companyService.getAll(page_number);
    }

    /**
     * Endpoint to get movies by Production Company
     * @param company_id
     * @return ResponseEntity (ok: List<movies>, no content)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/movies/{company_id}")
    @ApiOperation("Gets movies by Production Company. Authentication required (USER)")
    public ResponseEntity<?> getMovies(@PathVariable Long company_id){

        //Validates id
        if (!companyRepository.existsById(company_id))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        return companyService.getMovies(company_id);
    }

    /**
     * Endpoint to update the Production Company
     * Name, logoPath and OriginCountry are mandatory
     * Name and logoPath must be unique
     * @param company_id
     * @return ResponseEntity (ok: ProductionCompanyDto, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{company_id}")
    @ApiOperation("Updates the Production Company. Authentication required (ADMIN).\nName, logoPath and OriginCountry are mandatory. " +
            "Name and logoPath must be unique")
    public ResponseEntity<?> update(@PathVariable Long company_id, @Valid @RequestBody ProductionCompanyDto companyDto){

        //Validates id
        if (!companyRepository.existsById(company_id))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        return companyService.update(company_id, companyDto);
    }

    /**
     * Endpoint to delete the Production Company
     * @param company_id
     * @return ResponseEntity (messageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{company_id}")
    @ApiOperation("Deletes the Production Company. Authentication required (ADMIN)")
    public ResponseEntity<?> delete(@PathVariable Long company_id){

        //Validates id
        if (!companyRepository.existsById(company_id))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        return companyService.delete(company_id);
    }
}
