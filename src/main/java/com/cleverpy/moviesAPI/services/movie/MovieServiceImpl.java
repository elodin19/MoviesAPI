package com.cleverpy.moviesAPI.services.movie;

import com.cleverpy.moviesAPI.builders.MovieBuilder;
import com.cleverpy.moviesAPI.dto.MovieDto;
import com.cleverpy.moviesAPI.entities.*;
import com.cleverpy.moviesAPI.repositories.*;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the Movie Service Interface
 */
@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private final MovieRepository movieRepository;

    @Autowired
    private final GenreRepository genreRepository;

    @Autowired
    private final SpokenLanguageRepository languageRepository;

    @Autowired
    private final ProductionCountryRepository countryRepository;

    @Autowired
    private final ProductionCompanyRepository companyRepository;

    public MovieServiceImpl(MovieRepository movieRepository, GenreRepository genreRepository,
                            SpokenLanguageRepository languageRepository, ProductionCountryRepository countryRepository,
                            ProductionCompanyRepository companyRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.languageRepository = languageRepository;
        this.countryRepository = countryRepository;
        this.companyRepository = companyRepository;
    }

    /**
     * Method to create a new Movie
     * If genre, production company, production country or spoken language don't exist, create new ones
     * Fields ImdbId, Overview and PosterPath must be unique
     * @param movieDto
     * @return ResponseEntity (ok: movie, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> create(MovieDto movieDto) {

        //Tests if a value that's supposed to be unique is being duplicated
        if (movieRepository.existsByImdbId(movieDto.getImdbId())
                && movieRepository.existsByOverview(movieDto.getOverview())
                && movieRepository.existsByPosterPath(movieDto.getPosterPath())
        )
            return ResponseEntity.badRequest().body(new MessageResponse("Duplicated values. Fields ImdbId, Overview, PosterPath and Title must be unique"));

        //Validates genres, companies, countries and languages
        List<Genre> genres = new ArrayList<>();
        for (Genre genre : movieDto.getGenres()){
            Optional<Genre> genreOpt = genreRepository.findByName(genre.getName());

            if (genreOpt.isPresent()) genres.add(genreOpt.get());
            else {
                genres.add(genre);
                genreRepository.save(genre);
            }
        }

        List<ProductionCompany> companies = new ArrayList<>();
        for (ProductionCompany company : movieDto.getProductionCompanies()){
            Optional<ProductionCompany> companyOpt = companyRepository.findByName(company.getName());

            if (companyOpt.isPresent()) companies.add(companyOpt.get());
            else {
                company.setId(null);
                companies.add(company);
                companyRepository.save(company);
            }
        }

        List<ProductionCountry> countries = new ArrayList<>();
        for (ProductionCountry country : movieDto.getProductionCountries()){
            Optional<ProductionCountry> countryOpt = countryRepository.findByName(country.getName());

            if (countryOpt.isPresent()) countries.add(countryOpt.get());
            else {
                country.setId(null);
                countries.add(country);
                countryRepository.save(country);
            }
        }

        List<SpokenLanguage> languages = new ArrayList<>();
        for (SpokenLanguage language : movieDto.getSpokenLanguages()){
            Optional<SpokenLanguage> languageOpt = languageRepository.findByName(language.getName());

            if (languageOpt.isPresent()) languages.add(languageOpt.get());
            else {
                language.setId(null);
                languages.add(language);
                languageRepository.save(language);
            }
        }

        Movie movie = new MovieBuilder()
                .setAdult(movieDto.getAdult())
                .setBackdropPath(movieDto.getBackdropPath())
                .setBudget(movieDto.getBudget())
                .setHomepage(movieDto.getHomepage())
                .setImdbId(movieDto.getImdbId())
                .setOriginalLanguage(movieDto.getOriginalLanguage())
                .setOriginalTitle(movieDto.getOriginalTitle())
                .setOverview(movieDto.getOverview())
                .setPopularity(movieDto.getPopularity())
                .setPosterPath(movieDto.getPosterPath())
                .setReleaseDate(movieDto.getReleaseDate())
                .setRevenue(movieDto.getRevenue())
                .setRuntime(movieDto.getRuntime())
                .setStatus(movieDto.getStatus())
                .setTagline(movieDto.getTagline())
                .setTitle(movieDto.getTitle())
                .setVideo(movieDto.getVideo())
                .setVoteAverage(movieDto.getVoteAverage())
                .setVoteCount(movieDto.getVoteCount())
                .setProductionCompanies(companies)
                .setProductionCountries(countries)
                .setSpokenLanguages(languages)
                .setGenres(genres)
                .build();

        movieRepository.save(movie);
        return ResponseEntity.ok(movie);
    }

    /**
     * Method to fina a movie by id
     * @param id
     * @return ResponseEntity (ok: Movie)
     */
    @Override
    public ResponseEntity<?> getById(Long id) {

        Optional<Movie> movieOpt = movieRepository.findById(id);
        return ResponseEntity.ok(movieOpt.get());
    }

    /**
     * Method to get a Movie by filters
     * @param adult
     * @param video
     * @param budget
     * @param original_language
     * @param title
     * @param page_number
     * @return ResponseEntity (ok: Page<Movie>, no content)
     */
    @Override
    public ResponseEntity<?> getFiltered(Boolean adult, Boolean video, Long budget, String original_language, String title, Integer page_number) {

        Pageable page = PageRequest.of(page_number, 10);

        //Creates Movie with the values of the filters
        Movie movieWithFilters = new MovieBuilder()
                .setAdult(adult)
                .setVideo(video)
                .setBudget(budget)
                .setOriginalLanguage(original_language)
                .setTitle(title)
                .build();

        //Creates query
        final ExampleMatcher example = ExampleMatcher.matching().withIgnoreNullValues();
        final Example<Movie> query = Example.of(movieWithFilters, example);
        final Page<Movie> movies = movieRepository.findAll(query, page);

        if (movies == null)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(movies);
    }

    /**
     * Method to update a Movie
     * Fields ImdbId, Overview and PosterPath must be unique
     * @param id
     * @param movieDto
     * @return ResponseEntity (ok: Movie, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> update(Long id, MovieDto movieDto) {

        //Gets the Movie to be updated
        Optional<Movie> movieOpt = movieRepository.findById(id);

        //Tests if a value that's supposed to be unique is being duplicated
        if ((movieRepository.existsByImdbId(movieDto.getImdbId()) && !movieOpt.get().getImdbId().equalsIgnoreCase(movieDto.getImdbId()))
            || (movieRepository.existsByOverview(movieDto.getOverview()) && !movieOpt.get().getOverview().equalsIgnoreCase(movieDto.getOverview()))
            || (movieRepository.existsByPosterPath(movieDto.getPosterPath()) && !movieOpt.get().getPosterPath().equalsIgnoreCase(movieDto.getPosterPath()))
        )
            return ResponseEntity.badRequest().body(new MessageResponse("Duplicated values. Fields ImdbId, Overview and PosterPath must be unique"));

        //Validates genres, companies, countries and languages
        List<Genre> genres = new ArrayList<>();
        for (Genre genre : movieDto.getGenres()){
            Optional<Genre> genreOpt = genreRepository.findByName(genre.getName());

            if (genreOpt.isPresent()) genres.add(genreOpt.get());
            else {
                genres.add(genre);
                genreRepository.save(genre);
            }
        }

        List<ProductionCompany> companies = new ArrayList<>();
        for (ProductionCompany company : movieDto.getProductionCompanies()){
            Optional<ProductionCompany> companyOpt = companyRepository.findByName(company.getName());

            if (companyOpt.isPresent()) companies.add(companyOpt.get());
            else {
                company.setId(1L);
                companies.add(company);
                companyRepository.save(company);
            }
        }

        List<ProductionCountry> countries = new ArrayList<>();
        for (ProductionCountry country : movieDto.getProductionCountries()){
            Optional<ProductionCountry> countryOpt = countryRepository.findByName(country.getName());

            if (countryOpt.isPresent()) countries.add(countryOpt.get());
            else {
                country.setId(null);
                countries.add(country);
                countryRepository.save(country);
            }
        }

        List<SpokenLanguage> languages = new ArrayList<>();
        for (SpokenLanguage language : movieDto.getSpokenLanguages()){
            Optional<SpokenLanguage> languageOpt = languageRepository.findByName(language.getName());

            if (languageOpt.isPresent()) languages.add(languageOpt.get());
            else {
                language.setId(null);
                languages.add(language);
                languageRepository.save(language);
            }
        }

        //Updates and saves
        movieOpt.get().setAdult(movieDto.getAdult());
        movieOpt.get().setBackdropPath(movieDto.getBackdropPath());
        movieOpt.get().setBudget(movieDto.getBudget());
        movieOpt.get().setHomepage(movieDto.getHomepage());
        movieOpt.get().setImdbId(movieDto.getImdbId());
        movieOpt.get().setOriginalLanguage(movieDto.getOriginalLanguage());
        movieOpt.get().setOriginalTitle(movieDto.getOriginalTitle());
        movieOpt.get().setOverview(movieDto.getOverview());
        movieOpt.get().setPopularity(movieDto.getPopularity());
        movieOpt.get().setPosterPath(movieDto.getPosterPath());
        movieOpt.get().setReleaseDate(movieDto.getReleaseDate());
        movieOpt.get().setRevenue(movieDto.getRevenue());
        movieOpt.get().setRuntime(movieDto.getRuntime());
        movieOpt.get().setStatus(movieDto.getStatus());
        movieOpt.get().setTagline(movieDto.getTagline());
        movieOpt.get().setTitle(movieDto.getTitle());
        movieOpt.get().setVideo(movieDto.getVideo());
        movieOpt.get().setVoteAverage(movieDto.getVoteAverage());
        movieOpt.get().setVoteCount(movieDto.getVoteCount());
        movieOpt.get().setProductionCompanies(companies);
        movieOpt.get().setGenres(genres);
        movieOpt.get().setProductionCountries(countries);
        movieOpt.get().setSpokenLanguages(languages);
        movieRepository.save(movieOpt.get());
        return ResponseEntity.ok(movieOpt.get());
    }

    /**
     * Method to delete a movie
     * @param id
     * @return ResponseEntity (ok: messageResponse)
     */
    @Override
    public ResponseEntity<?> delete(Long id) {

        Optional<Movie> movieOpt = movieRepository.findById(id);
        movieRepository.delete(movieOpt.get());
        return ResponseEntity.ok(new MessageResponse("Movie " + id + " deleted with success"));
    }

}
