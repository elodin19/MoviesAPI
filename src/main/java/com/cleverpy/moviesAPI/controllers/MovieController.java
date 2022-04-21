package com.cleverpy.moviesAPI.controllers;

import com.cleverpy.moviesAPI.dto.MovieDto;
import com.cleverpy.moviesAPI.repositories.MovieRepository;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import com.cleverpy.moviesAPI.services.movie.MovieServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller to manage the CRUD operations of the Movies
 * Requires authentication
 */
@RestController
@RequestMapping("/api/movie")
public class MovieController {

    private final MovieServiceImpl movieService;
    private final MovieRepository movieRepository;

    public MovieController(MovieServiceImpl movieService, MovieRepository movieRepository) {
        this.movieService = movieService;
        this.movieRepository = movieRepository;
    }

    /**
     * Endpoint to create a new Movie
     * @param movieDto
     * @return ResponseEntity(ok: movie, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/")
    @ApiOperation("Creates new Movie. Authentication required (USER)")
    public ResponseEntity<?> createMovie(@Valid @RequestBody MovieDto movieDto){
        return movieService.create(movieDto);
    }

    /**
     * Endpoint to get a Movie by id
     * @param movie_id
     * @return ResponseEntity (ok: Genre, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{movie_id}")
    @ApiOperation("Gets a Movie by id. Authentication required (USER)")
    public ResponseEntity<?> getById(@PathVariable Long movie_id){

        //Validates the id
        if (!movieRepository.existsById(movie_id))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        return movieService.getById(movie_id);
    }

    /**
     * Endpoint to get a Movie by filter
     * If a request comes with no filter, gets all movies
     * @param adult
     * @param video
     * @param budget
     * @param original_language
     * @param title
     * @param page_number
     * @return ResponseEntity (ok: Page<Movie>, no content)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/filters/{page_number}")
    @ApiOperation("Gets all Movies filtered. Authentication required (USER)")
    public ResponseEntity<?> getFiltered(
            @RequestParam(required = false) Boolean adult,
            @RequestParam(required = false) Boolean video,
            @RequestParam(required = false) Long budget,
            @RequestParam(required = false) String original_language,
            @RequestParam(required = false) String title,
            @PathVariable Integer page_number
            ){
        return movieService.getFiltered(adult, video, budget, original_language, title, page_number);
    }

    /**
     * Endpoint to update the Movie
     * @param movie_id
     * @return ResponseEntity (ok: Movie, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{movie_id}")
    @ApiOperation("Updates the Movie. Authentication required (ADMIN)")
    public ResponseEntity<?> update(@PathVariable Long movie_id, @Valid @RequestBody MovieDto movieDto){

        //Validates id
        if (!movieRepository.existsById(movie_id))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        return movieService.update(movie_id, movieDto);
    }

    /**
     * Endpoint to delete the Movie
     * @param movie_id
     * @return ResponseEntity (messageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{movie_id}")
    @ApiOperation("Deletes the Movie. Authentication required (ADMIN)")
    public ResponseEntity<?> delete(@PathVariable Long movie_id){

        //Validates id
        if (!movieRepository.existsById(movie_id))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        return movieService.delete(movie_id);
    }
}
