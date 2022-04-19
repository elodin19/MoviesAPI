package com.cleverpy.moviesAPI.controllers;

import com.cleverpy.moviesAPI.dto.genre.GenreDto;
import com.cleverpy.moviesAPI.repositories.GenreRepository;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import com.cleverpy.moviesAPI.services.genre.GenreServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * Controller to manage the CRUD operations of the Genres
 * Requires authentication
 */
@RestController
@RequestMapping("/api/genre")
public class GenreController {

    private final GenreServiceImpl genreService;
    private final GenreRepository genreRepository;

    public GenreController(GenreServiceImpl genreService, GenreRepository genreRepository) {
        this.genreService = genreService;
        this.genreRepository = genreRepository;
    }

    /**
     * Endpoint to create a new Genre
     * Name is mandatory and must be unique
     * @param genreDto
     * @return ResponseEntity (ok: genreDto, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/")
    @ApiOperation("Creates new Genre. Authentication required (ADMIN). Name is mandatory and must be unique")
    public ResponseEntity<?> createGenre(@Valid @RequestBody GenreDto genreDto){
        return genreService.create(genreDto);
    }

    /**
     * Endpoint to get a Genre by id
     * @param genre_id
     * @return ResponseEntity (ok: genreDto, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{genre_id}")
    @ApiOperation("Gets a Genre by id. Authentication required (USER)")
    public ResponseEntity<?> getById(@PathVariable Long genre_id){

        //Validates the id
        if (!genreRepository.existsById(genre_id))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        return genreService.getById(genre_id);
    }

    /**
     * Endpoint to get all Genres
     * @return ResponseEntity (ok: GenresPageDto, no content)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/page/{page_number}")
    @ApiOperation("Gets all Genres. Authentication required (USER)")
    public ResponseEntity<?> getAll(@PathVariable Integer page_number){
        return genreService.getAll(page_number);
    }

    /**
     * Endpoint to get movies by Genre
     * @param genre_id
     * @return ResponseEntity (ok: List<movies>, no content)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/movies/{genre_id}")
    @ApiOperation("Gets movies by Genre. Authentication required (USER)")
    public ResponseEntity<?> getMovies(@PathVariable Long genre_id){

        //Validates id
        if (!genreRepository.existsById(genre_id))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        return genreService.getMovies(genre_id);
    }

    /**
     * Endpoint to update the Genre
     * Name is mandatory and must be unique
     * @param genre_id
     * @return ResponseEntity (ok: GenreDto, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{genre_id}")
    @ApiOperation("Updates the Genre. Authentication required (ADMIN). Name is mandatory and must be unique")
    public ResponseEntity<?> update(@PathVariable Long genre_id, @Valid @RequestBody GenreDto genreDto){

        //Validates id
        if (!genreRepository.existsById(genre_id))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        return genreService.update(genre_id, genreDto);
    }

    /**
     * Endpoint to delete the Genre
     * @param genre_id
     * @return ResponseEntity (messageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{genre_id}")
    @ApiOperation("Deletes the Genre. Authentication required (ADMIN)")
    public ResponseEntity<?> delete(@PathVariable Long genre_id){

        //Validates id
        if (!genreRepository.existsById(genre_id))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        return genreService.delete(genre_id);
    }}
