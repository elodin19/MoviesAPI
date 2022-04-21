package com.cleverpy.moviesAPI.services.spokenLanguage;

import com.cleverpy.moviesAPI.dto.SpokenLanguageDto;
import org.springframework.http.ResponseEntity;

/**
 * Spoken Language Service Interface
 */
public interface SpokenLanguageService {

    ResponseEntity<?> create(SpokenLanguageDto languageDto);
    ResponseEntity<?> getById(Long id);
    ResponseEntity<?> getAll(Integer pageNumber);
    ResponseEntity<?> update(Long id, SpokenLanguageDto languageDto);
    ResponseEntity<?> delete(Long id);
}
