package com.cleverpy.moviesAPI.services.genre;

import com.cleverpy.moviesAPI.dto.GenreDto;
import org.springframework.http.ResponseEntity;

/**
 * Genre Service Interface
 */
public interface GenreService {

    ResponseEntity<?> create(GenreDto genreDto);
    ResponseEntity<?> getById(Long id);
    ResponseEntity<?> getAll(Integer pageNumber);
    ResponseEntity<?> update(Long id, GenreDto genreDto);
    ResponseEntity<?> delete(Long id);

}
