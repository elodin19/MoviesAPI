package com.cleverpy.moviesAPI.services.spokenLanguage;

import com.cleverpy.moviesAPI.dto.spokenLanguage.SpokenLanguageDto;
import com.cleverpy.moviesAPI.entities.Movie;
import com.cleverpy.moviesAPI.entities.SpokenLanguage;
import com.cleverpy.moviesAPI.repositories.MovieRepository;
import com.cleverpy.moviesAPI.repositories.SpokenLanguageRepository;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the Spoken Language Service
 */
@Service
public class SpokenLanguageServiceImpl implements SpokenLanguageService {

    @Autowired
    private SpokenLanguageRepository languageRepository;

    @Autowired
    private MovieRepository movieRepository;

    public SpokenLanguageServiceImpl(SpokenLanguageRepository languageRepository, MovieRepository movieRepository) {
        this.languageRepository = languageRepository;
        this.movieRepository = movieRepository;
    }

    /**
     * Method to create a new Spoken Language
     * Tests if name and iso aren't being used yet
     * @param languageDto
     * @return ResponseEntity (ok: spokenLanguageDto, bad request: messageResponse)
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
        SpokenLanguage language = new SpokenLanguage(null, languageDto.getEnglishName(), languageDto.getIso(),
                languageDto.getName(), null);
        languageRepository.save(language);

        return ResponseEntity.ok(language.getDto());
    }

    /**
     * Method to get the Spoken Language data
     * @param id
     * @return ResponseEntity (ok: spokenLanguageDto, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> getById(Long id) {

        //Gets the language
        Optional<SpokenLanguage> languageOpt = languageRepository.findById(id);

        return ResponseEntity.ok(languageOpt.get().getDto());
    }

    /**
     * Method to get all Spoken Languages
     * @return ResponseEntity (List<spokenLanguageDto>, no content)
     */
    @Override
    public ResponseEntity<?> getAll() {

        List<SpokenLanguage> languages = languageRepository.findAll();

        if (languages.size() == 0)
            return ResponseEntity.noContent().build();

        List<SpokenLanguageDto> languagesDto = new ArrayList<>();
        for (SpokenLanguage language : languages) languagesDto.add(language.getDto());

        return ResponseEntity.ok(languagesDto);
    }

    /**
     * Method to get all the movies from a specific Spoken Language
     * @param id
     * @return ResponseEntity (ok: List<Movie>, no content)
     */
    @Override
    public ResponseEntity<?> getMovies(Long id) {

        //Gets the language
        Optional<SpokenLanguage> languageOpt = languageRepository.findById(id);

        if (languageOpt.get().getMovies().size() == 0)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(languageOpt.get().getMovies());
    }

    /**
     * Method to update the Spoken Language
     * Tests if name and iso aren't being used yet
     * @param id
     * @param languageDto
     * @return ResponseEntity (ok: languageDto, bad request: messageResponse)
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
        languageOpt.get().setEnglishName(languageDto.getEnglishName());
        languageRepository.save(languageOpt.get());

        return ResponseEntity.ok(languageOpt.get().getDto());

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

        //Check the movies to see if they need to be removed too
        for (Movie movie : languageOpt.get().getMovies()){
            for (SpokenLanguage language : movie.getSpokenLanguages()){
                if (language.equals(languageOpt.get())){
                    movie.getSpokenLanguages().remove(languageOpt.get());
                    if (movie.getSpokenLanguages().size() == 0)
                        movieRepository.delete(movie);
                    else
                        movieRepository.save(movie);
                }
            }
        }

        languageRepository.delete(languageOpt.get());
        return ResponseEntity.ok(new MessageResponse("Spoken Language " + id + " deleted with success"));
    }
}
