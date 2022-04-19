package com.cleverpy.moviesAPI.controllers;

import com.cleverpy.moviesAPI.dto.productionCountry.ProductionCountryDto;
import com.cleverpy.moviesAPI.dto.spokenLanguage.SpokenLanguageDto;
import com.cleverpy.moviesAPI.repositories.SpokenLanguageRepository;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import com.cleverpy.moviesAPI.services.spokenLanguage.SpokenLanguageServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller to manage the CRUD operations of the Spoken Languages
 * Requires authentication
 */
@RestController
@RequestMapping("/api/language")
public class SpokenLanguageController {

    private final SpokenLanguageServiceImpl languageService;
    private final SpokenLanguageRepository languageRepository;

    public SpokenLanguageController(SpokenLanguageServiceImpl languageService, SpokenLanguageRepository languageRepository) {
        this.languageService = languageService;
        this.languageRepository = languageRepository;
    }

    /**
     * Endpoint to create a new Spoken Language
     * Name, englishName and iso are mandatory
     * Name and iso must be unique
     * @param languageDto
     * @return ResponseEntity (ok: SpokenLanguageDto, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/")
    @ApiOperation("Creates new Spoken Language. Authentication required (ADMIN). Name, englishName and iso are mandatory. " +
            "Name and iso must be unique")
    public ResponseEntity<?> createLanguage(@Valid @RequestBody SpokenLanguageDto languageDto){
        return languageService.create(languageDto);
    }

    /**
     * Endpoint to get a Spoken Language by id
     * @param language_id
     * @return ResponseEntity (ok: SpokenLanguageDto, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{language_id}")
    @ApiOperation("Gets a Spoken Language by id. Authentication required (USER)")
    public ResponseEntity<?> getById(@PathVariable Long language_id){

        //Validates the id
        if (!languageRepository.existsById(language_id))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        return languageService.getById(language_id);
    }

    /**
     * Endpoint to get all Production Countries
     * @return ResponseEntity (ok: SpokenLanguagesPageDto, no content)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/page/{page_number}")
    @ApiOperation("Gets all Spoken Languages. Authentication required (USER)")
    public ResponseEntity<?> getAll(@PathVariable Integer page_number){
        return languageService.getAll(page_number);
    }

    /**
     * Endpoint to get movies by Production Country
     * @param language_id
     * @return ResponseEntity (ok: List<movies>, no content)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/movies/{language_id}")
    @ApiOperation("Gets movies by Spoken Language. Authentication required (USER)")
    public ResponseEntity<?> getMovies(@PathVariable Long language_id){

        //Validates id
        if (!languageRepository.existsById(language_id))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        return languageService.getMovies(language_id);
    }

    /**
     * Endpoint to update the Spoken Language
     * Name, englishName and iso are mandatory
     * Name and iso must be unique
     * @param language_id
     * @param languageDto
     * @return ResponseEntity (ok: ProductionCountryDto, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{language_id}")
    @ApiOperation("Updates the Spoken Language. Authentication required (ADMIN). Name, englishName and iso are mandatory. " +
            "Name and iso must be unique")
    public ResponseEntity<?> update(@PathVariable Long language_id, @Valid @RequestBody SpokenLanguageDto languageDto){

        //Validates id
        if (!languageRepository.existsById(language_id))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        return languageService.update(language_id, languageDto);
    }

    /**
     * Endpoint to delete the Spoken Language
     * @param language_id
     * @return ResponseEntity (messageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{language_id}")
    @ApiOperation("Deletes the Spoken Language. Authentication required (ADMIN)")
    public ResponseEntity<?> delete(@PathVariable Long language_id) {

        //Validates id
        if (!languageRepository.existsById(language_id))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        return languageService.delete(language_id);
    }
}
