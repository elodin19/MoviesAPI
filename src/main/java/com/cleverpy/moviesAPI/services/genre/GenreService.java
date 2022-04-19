package com.cleverpy.moviesAPI.services.genre;

import com.cleverpy.moviesAPI.dto.genre.GenreDto;
import org.springframework.http.ResponseEntity;

/**
 * Genre Service Interface
 */
public interface GenreService {

    ResponseEntity<?> create(GenreDto genreDto);
    ResponseEntity<?> getById(Long id);
    ResponseEntity<?> getAll(Integer pageNumber);
    ResponseEntity<?> getMovies(Long id);
    ResponseEntity<?> update(Long id, GenreDto genreDto);
    ResponseEntity<?> delete(Long id);

}
