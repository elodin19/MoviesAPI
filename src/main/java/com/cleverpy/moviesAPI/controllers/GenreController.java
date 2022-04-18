package com.cleverpy.moviesAPI.controllers;

/**
 * Controller to manage the CRUD operations of the Genres
 * Requires authentication
 */

import com.cleverpy.moviesAPI.dto.genre.GenreDto;
import com.cleverpy.moviesAPI.repositories.GenreRepository;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import com.cleverpy.moviesAPI.services.genre.GenreServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
     * Endpoint to create a new genre
     * @param genreDto
     * @return ResponseEntity (ok: genreDto, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/")
    @ApiOperation("Creates new genre. Authentication required (ADMIN)")
    public ResponseEntity<?> createGenre(@RequestBody GenreDto genreDto){

        //Validates Dto
        if (genreDto.getName() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Missing parameters"));

        return genreService.createGenre(genreDto);
    }

    /**
     * Endpoint to get a genre by id
     * @param genre_id
     * @return ResponseEntity (ok: genreDto, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{genre_id}")
    @ApiOperation("Gets a genre by id. Authentication required (USER)")
    public ResponseEntity<?> getGenre(@PathVariable Long genre_id){

        //Validates the id
        if (!genreRepository.existsById(genre_id))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        return genreService.getGenre(genre_id);
    }

    /**
     * Endpoint to get all genres
     * @return ResponseEntity (ok: List<genreDto>, no content)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/")
    @ApiOperation("Gets all genres. Authentication required (USER)")
    public ResponseEntity<?> getAllGenres(){

        return genreService.getAllGenres();
    }

    /**
     * Endpoint to get movies by genre
     * @param genre_id
     * @return ResponseEntity (ok: List<movies>, no content)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/movies/{genre_id}")
    @ApiOperation("Gets movies by genre. Authentication required (USER)")
    public ResponseEntity<?> getMoviesFromGenre(@PathVariable Long genre_id){

        //Validates id
        if (!genreRepository.existsById(genre_id))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        return genreService.getMoviesFromGenre(genre_id);
    }

    /**
     * Endpoint to update the genre name
     * @param genre_id
     * @return ResponseEntity (ok: GenreDto, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{genre_id}")
    @ApiOperation("Updates the genre name. Authentication required (ADMIN)")
    public ResponseEntity<?> updateGenre(@PathVariable Long genre_id, @RequestBody GenreDto genreDto){

        //Validates id
        if (!genreRepository.existsById(genre_id))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        //Validates Dto
        if (genreDto.getName() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Missing parameters"));

        return genreService.updateGenre(genre_id, genreDto);
    }

    /**
     * Endpoint to delete the genre name
     * @param genre_id
     * @return ResponseEntity (messageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{genre_id}")
    @ApiOperation("Deletes the genre name. Authentication required (ADMIN)")
    public ResponseEntity<?> deleteGenre(@PathVariable Long genre_id){

        //Validates id
        if (!genreRepository.existsById(genre_id))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        return genreService.deleteGenre(genre_id);
    }}
