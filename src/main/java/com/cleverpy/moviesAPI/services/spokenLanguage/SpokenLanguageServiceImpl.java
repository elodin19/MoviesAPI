package com.cleverpy.moviesAPI.services.spokenLanguage;

import com.cleverpy.moviesAPI.dto.SpokenLanguageDto;
import com.cleverpy.moviesAPI.entities.SpokenLanguage;
import com.cleverpy.moviesAPI.repositories.SpokenLanguageRepository;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the Spoken Language Service
 */
@Service
public class SpokenLanguageServiceImpl implements SpokenLanguageService {

    @Autowired
    private SpokenLanguageRepository languageRepository;

    public SpokenLanguageServiceImpl(SpokenLanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    /**
     * Method to create a new Spoken Language
     * Tests if name and iso aren't being used yet
     * @param languageDto
     * @return ResponseEntity (ok: SpokenLanguage, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> create(SpokenLanguageDto languageDto) {

        //Tests if the language name already exists
        if (languageRepository.existsByName(languageDto.getName()))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The spoken language " + languageDto.getName() + " is already registered"));

        //Tests if the iso already exists
        if (languageRepository.existsByIso(languageDto.getIso()))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The iso " + languageDto.getIso() + " is already registered"));

        //Creates and saves the new language
        SpokenLanguage language = new SpokenLanguage(null, languageDto.getEnglish_name(), languageDto.getIso(),
                languageDto.getName());
        languageRepository.save(language);

        return ResponseEntity.ok(language);
    }

    /**
     * Method to get the Spoken Language data
     * @param id
     * @return ResponseEntity (ok: spokenLanguage, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> getById(Long id) {

        //Gets the language
        Optional<SpokenLanguage> languageOpt = languageRepository.findById(id);

        return ResponseEntity.ok(languageOpt.get());
    }

    /**
     * Method to get all Spoken Languages
     * @return ResponseEntity (Page<SpokenLanguages>, no content)
     */
    @Override
    public ResponseEntity<?> getAll(Integer pageNumber) {

        Pageable page = PageRequest.of(pageNumber, 10);
        Page<SpokenLanguage> languages = languageRepository.findAll(page);

        if (languages.toList().size() == 0)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(languages);
    }

    /**
     * Method to update the Spoken Language
     * Tests if name and iso aren't being used yet
     * @param id
     * @param languageDto
     * @return ResponseEntity (ok: SpokenLanguage, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> update(Long id, SpokenLanguageDto languageDto) {

        //Gets the language
        Optional<SpokenLanguage> languageOpt = languageRepository.findById(id);

        //Tests if name and iso already exists
        if (languageRepository.existsByName(languageDto.getName()) && !languageOpt.get().getName().equalsIgnoreCase(languageDto.getName()))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The spoken language " + languageDto.getName() + " is already registered"));

        if (languageRepository.existsByIso(languageDto.getIso()) && !languageOpt.get().getIso().equalsIgnoreCase(languageDto.getIso()))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The iso " + languageDto.getIso() + " is already registered"));

        //Updates and saves the spoken language
        languageOpt.get().setName(languageDto.getName());
        languageOpt.get().setIso(languageDto.getIso());
        languageOpt.get().setEnglish_name(languageDto.getEnglish_name());
        languageRepository.save(languageOpt.get());

        return ResponseEntity.ok(languageOpt.get());

    }

    /**
     * Method to delete a Spoken Language
     * If a movie is associated to only this Spoken Language, the movie is removed too
     * @param id
     * @return ResponseEntity(MessageResponse)
     */
    @Override
    public ResponseEntity<?> delete(Long id) {

        //Gets the language
        Optional<SpokenLanguage> languageOpt = languageRepository.findById(id);

        languageRepository.delete(languageOpt.get());
        return ResponseEntity.ok(new MessageResponse("Spoken Language " + id + " deleted with success"));
    }
}
