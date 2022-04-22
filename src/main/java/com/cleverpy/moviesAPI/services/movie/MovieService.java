package com.cleverpy.moviesAPI.services.movie;

import com.cleverpy.moviesAPI.dto.MovieDto;
import org.springframework.http.ResponseEntity;

/**
 * Movie Service Interface
 */
public interface MovieService {

    ResponseEntity<?> create(MovieDto movieDto);
    ResponseEntity<?> getById(Long id);
    ResponseEntity<?> getFiltered(Boolean adult, Boolean video, Long budget, String original_language, String title, Integer page_number);
    ResponseEntity<?> update(Long id, MovieDto movieDto);
    ResponseEntity<?> delete(Long id);

}
