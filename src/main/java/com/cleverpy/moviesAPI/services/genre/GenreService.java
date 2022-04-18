package com.cleverpy.moviesAPI.services.genre;

import com.cleverpy.moviesAPI.dto.genre.GenreDto;
import org.springframework.http.ResponseEntity;

/**
 * Genre Service Interface
 */
public interface GenreService {

    ResponseEntity<?> createGenre(GenreDto genreDto);
    ResponseEntity<?> getGenre(Long id);
    ResponseEntity<?> getAllGenres();
    ResponseEntity<?> getMoviesFromGenre(Long id);
    ResponseEntity<?> updateGenre(Long id, GenreDto genreDto);
    ResponseEntity<?> deleteGenre(Long id);
}
