package com.cleverpy.moviesAPI.services.genre;

import com.cleverpy.moviesAPI.dto.GenreDto;
import com.cleverpy.moviesAPI.entities.Genre;
import com.cleverpy.moviesAPI.repositories.GenreRepository;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the Genre Service Interface
 */
@Service
public class GenreServiceImpl implements GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    /**
     * Method to create a new genre
     * Tests if the name isn't being used yet
     * @param genreDto
     * @return ResponseEntity (ok: Genre, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> create(GenreDto genreDto) {

        //Tests if the genre already exists
        if (genreRepository.existsByName(genreDto.getName()))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The genre " + genreDto.getName() + " is already registered"));

        //Creates and saves the new genre
        Genre genre = new Genre(null, genreDto.getName());
        genreRepository.save(genre);

        return ResponseEntity.ok(genre);
    }

    /**
     * Method to get the genre data
     * @param id
     * @return ResponseEntity (ok: Genre)
     */
    @Override
    public ResponseEntity<?> getById(Long id) {

        Optional<Genre> genreOpt = genreRepository.findById(id);
        return ResponseEntity.ok(genreOpt.get());
    }

    /**
     * Method to get all genres
     * @return ResponseEntity (ok: Page<Genre>, no content)
     */
    @Override
    public ResponseEntity<?> getAll(Integer pageNumber) {

        Pageable page = PageRequest.of(pageNumber, 10);
        Page<Genre> genres = genreRepository.findAll(page);

        if (genres.toList().size() == 0)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(genres);
    }

    /**
     * Method to update the name of a genre
     * Tests if the name isn't being used yet
     * @param id
     * @param genreDto
     * @return ResponseEntity (ok: Genre, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> update(Long id, GenreDto genreDto) {

        //Tests if the new name already exists
        if (genreRepository.existsByName(genreDto.getName()))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The genre " + genreDto.getName() + " is already registered"));

        //Gets the genre to be updated
        Optional<Genre> genreOpt = genreRepository.findById(id);

        //Updates the name
        genreOpt.get().setName(genreDto.getName());
        genreRepository.save(genreOpt.get());

        return ResponseEntity.ok(genreOpt.get());
    }

    /**
     * Method to delete a genre
     * @param id
     * @return ResponseEntity(MessageResponse)
     */
    @Override
    public ResponseEntity<?> delete(Long id) {

        Optional<Genre> genreOpt = genreRepository.findById(id);
        genreRepository.delete(genreOpt.get());
        return ResponseEntity.ok(new MessageResponse("Genre " + id + " deleted with success"));
    }
}
