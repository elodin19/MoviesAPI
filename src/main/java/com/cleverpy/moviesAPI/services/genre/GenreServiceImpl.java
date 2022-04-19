package com.cleverpy.moviesAPI.services.genre;

import com.cleverpy.moviesAPI.dto.genre.GenreDto;
import com.cleverpy.moviesAPI.entities.Genre;
import com.cleverpy.moviesAPI.entities.Movie;
import com.cleverpy.moviesAPI.repositories.GenreRepository;
import com.cleverpy.moviesAPI.repositories.MovieRepository;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the Genre Service Interface
 */
@Service
public class GenreServiceImpl implements GenreService {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MovieRepository movieRepository;

    public GenreServiceImpl(GenreRepository genreRepository, MovieRepository movieRepository) {
        this.genreRepository = genreRepository;
        this.movieRepository = movieRepository;
    }

    /**
     * Method to create a new genre
     * @param genreDto
     * @return ResponseEntity (ok: genreDto, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> create(GenreDto genreDto) {

        //Tests if the genre already exists
        if (genreRepository.existsByName(genreDto.getName()))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The genre " + genreDto.getName() + " is already registered"));

        //Creates and saves the new genre
        Genre genre = new Genre(null, genreDto.getName(), null);
        genreRepository.save(genre);

        return ResponseEntity.ok(genre.getDto());
    }

    /**
     * Method to get the genre data
     * @param id
     * @return ResponseEntity (ok: genreDto, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> getById(Long id) {

        //Gets the genre
        Optional<Genre> genreOpt = genreRepository.findById(id);

        return ResponseEntity.ok(genreOpt.get().getDto());
    }

    /**
     * Method to get all genres
     * @return ResponseEntity (ok: List<genreDto>, no content)
     */
    @Override
    public ResponseEntity<?> getAll() {

        List<Genre> genres = genreRepository.findAll();

        if (genres.size() == 0)
            return ResponseEntity.noContent().build();

        List<GenreDto> genresDto = new ArrayList<>();
        for (Genre genre : genres) genresDto.add(genre.getDto());

        return ResponseEntity.ok(genresDto);
    }

    /**
     * Method to get all the movies from a specific genre
     * @param id
     * @return ResponseEntity (ok: List<Movie>, no content)
     */
    @Override
    public ResponseEntity<?> getMovies(Long id) {

        //Gets the genre
        Optional<Genre> genreOpt = genreRepository.findById(id);

        if (genreOpt.get().getMovies().size() == 0)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(genreOpt.get().getMovies());
    }

    /**
     * Method to update the name of a genre
     * @param id
     * @param genreDto
     * @return ResponseEntity (ok: genreDto, bad request: messageResponse)
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

        return ResponseEntity.ok(genreOpt.get().getDto());
    }

    /**
     * Method to delete a genre
     * If a movie is associated to only this genre, the movie is removed too
     * @param id
     * @return ResponseEntity(MessageResponse)
     */
    @Override
    public ResponseEntity<?> delete(Long id) {

        //Gets the genre to be removed
        Optional<Genre> genreOpt = genreRepository.findById(id);

        //Check the movies to see if they need to be removed too
        for (Movie movie : genreOpt.get().getMovies()){
            for (Genre genre : movie.getGenres()){
                if (genre.equals(genreOpt.get())) {
                    movie.getGenres().remove(genreOpt.get());
                    if (movie.getGenres().size() < 1)
                        movieRepository.delete(movie);
                }
            }
        }

        //Deletes the genre
        genreRepository.delete(genreOpt.get());

        return ResponseEntity.ok(new MessageResponse("Genre " + id + " deleted with success"));
    }
}
