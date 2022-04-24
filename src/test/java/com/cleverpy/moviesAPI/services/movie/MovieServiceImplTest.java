package com.cleverpy.moviesAPI.services.movie;

import com.cleverpy.moviesAPI.builders.MovieBuilder;
import com.cleverpy.moviesAPI.dto.MovieDto;
import com.cleverpy.moviesAPI.entities.*;
import com.cleverpy.moviesAPI.repositories.*;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    private MovieRepository repository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private SpokenLanguageRepository languageRepository;

    @Mock
    private ProductionCountryRepository countryRepository;

    @Mock
    private ProductionCompanyRepository companyRepository;

    @InjectMocks
    private MovieServiceImpl underTest;

    @BeforeEach
    void setUp() {
        repository = mock(MovieRepository.class);
        languageRepository = mock(SpokenLanguageRepository.class);
        countryRepository = mock(ProductionCountryRepository.class);
        companyRepository = mock(ProductionCompanyRepository.class);
        underTest = new MovieServiceImpl(repository, genreRepository, languageRepository, countryRepository, companyRepository);
    }

    @Test
    void create() {
        final Genre genre1 = new Genre(1L, "Action");
        final Genre genre2 = new Genre(2L, "Comedy");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre1);
        genres.add(genre2);

        final ProductionCompany company1 = new ProductionCompany(1L, "Pixar", "logo.png", "USA");
        final ProductionCompany company2 = new ProductionCompany(2L, "Pixar2", "logo.png", "USA");
        List<ProductionCompany> companies = new ArrayList<>();
        companies.add(company1);
        companies.add(company2);

        final ProductionCountry country1 = new ProductionCountry(1L, "us", "USA");
        final ProductionCountry country2 = new ProductionCountry(2L, "ca", "Canada");
        List<ProductionCountry> countries = new ArrayList<>();
        countries.add(country1);
        countries.add(country2);

        final SpokenLanguage language1 = new SpokenLanguage(1L, "english", "en", "english");
        final SpokenLanguage language2 = new SpokenLanguage(2L, "spanish", "es", "spanish");
        List<SpokenLanguage> languages = new ArrayList<>();
        languages.add(language1);
        languages.add(language2);

        final MovieDto dto = new MovieDto(true, "path", 30000L, "homepage", "imdb", "language", "title", "overview", 2000.5D,
                "poster.jpg", "1994-05-12", 5532L, 50, "status", "tagline", "title", false, 23213D, 888921L,
                companies, genres, countries, languages);

        when(repository.existsByImdbId(dto.getImdbId())).thenReturn(false);

        when(genreRepository.findByName(genre1.getName())).thenReturn(Optional.of(genre1));
        when(genreRepository.findByName(genre2.getName())).thenReturn(Optional.empty());

        when(companyRepository.findByName(company1.getName())).thenReturn(Optional.of(company1));
        when(companyRepository.findByName(company2.getName())).thenReturn(Optional.empty());

        when(countryRepository.findByName(country1.getName())).thenReturn(Optional.of(country1));
        when(countryRepository.findByName(country2.getName())).thenReturn(Optional.empty());

        when(languageRepository.findByName(language1.getName())).thenReturn(Optional.of(language1));
        when(languageRepository.findByName(language2.getName())).thenReturn(Optional.empty());

        final ResponseEntity<?> expected = underTest.create(dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(Movie.class);
        assertThat(expected.getBody()).asString().contains("adult=true");
        assertThat(expected.getBody()).asString().contains("budget=30000");
        assertThat(expected.getBody()).asString().contains("title=title");
    }

    @Test
    void shouldNotCreateDuplicatedValue() {
        final MovieDto dto = new MovieDto();
        dto.setImdbId("imbd");
        dto.setOverview("overview");
        dto.setPosterPath("poster.jpg");

        when(repository.existsByImdbId(dto.getImdbId())).thenReturn(true);
        when(repository.existsByOverview(dto.getOverview())).thenReturn(true);
        when(repository.existsByPosterPath(dto.getPosterPath())).thenReturn(true);
        final ResponseEntity<?> expected = underTest.create(dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(400);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("Duplicated values. Fields ImdbId, Overview, PosterPath and Title must be unique");
    }

    @Test
    void getById() {
        final Movie movie = new MovieBuilder()
                .setId(1L)
                .setAdult(false)
                .setBackdropPath("path")
                .setBudget(100000L)
                .setHomepage("homepage")
                .setImdbId("imdb")
                .setOriginalLanguage("language")
                .setOriginalTitle("title")
                .setOverview("overview")
                .setPopularity(222D)
                .setPosterPath("poster.png")
                .setTitle("title")
                .build();

        when(repository.findById(movie.getId())).thenReturn(Optional.of(movie));
        final ResponseEntity<?> expected = underTest.getById(movie.getId());

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(Movie.class);
        assertThat(expected.getBody()).asString().contains("adult=false");
        assertThat(expected.getBody()).asString().contains("budget=100000");
        assertThat(expected.getBody()).asString().contains("title=title");
    }

    @Test
    void getFiltered() {
        final Pageable page = PageRequest.of(0, 10);
        final Movie movie = new MovieBuilder()
                .setId(1L)
                .setAdult(false)
                .setVideo(false)
                .setBackdropPath("path")
                .setBudget(11111L)
                .setHomepage("homepage")
                .setImdbId("imdb")
                .setOriginalLanguage("language")
                .setOriginalTitle("title")
                .setOverview("overview")
                .setPopularity(222D)
                .setPosterPath("poster.png")
                .setTitle(null)
                .build();

        final Movie filters = new MovieBuilder()
                .setAdult(false)
                .setVideo(false)
                .setBudget(11111L)
                .setOriginalLanguage("language")
                .setTitle(null)
                .build();

        List<Movie> movies = new ArrayList<>();
        movies.add(movie);

        final ExampleMatcher example = ExampleMatcher.matching().withIgnoreNullValues();
        final Example<Movie> query = Example.of(filters, example);
        final Page<Movie> result = new PageImpl<>(movies);

        when(repository.findAll(query, page)).thenReturn(result);
        final ResponseEntity<?> expected = underTest.getFiltered(
                movie.getAdult(),
                movie.getVideo(),
                movie.getBudget(),
                movie.getOriginalLanguage(),
                movie.getTitle(),
                0
        );

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(Page.class);
    }

    @Test
    void getFilteredNull() {
        final Pageable page = PageRequest.of(0, 10);
        final Movie filters = new MovieBuilder()
                .setAdult(false)
                .setVideo(false)
                .setBudget(11111L)
                .setOriginalLanguage("language")
                .setTitle(null)
                .build();

        final ExampleMatcher example = ExampleMatcher.matching().withIgnoreNullValues();
        final Example<Movie> query = Example.of(filters, example);

        when(repository.findAll(query, page)).thenReturn(null);
        final ResponseEntity<?> expected = underTest.getFiltered(
                filters.getAdult(),
                filters.getVideo(),
                filters.getBudget(),
                filters.getOriginalLanguage(),
                filters.getTitle(),
                0
        );

        assertThat(expected.getStatusCodeValue()).isEqualTo(204);
    }

    @Test
    void update() {
        final Genre genre1 = new Genre(1L, "Action");
        final Genre genre2 = new Genre(2L, "Comedy");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre1);
        genres.add(genre2);

        final ProductionCompany company1 = new ProductionCompany(1L, "Pixar", "logo.png", "USA");
        final ProductionCompany company2 = new ProductionCompany(2L, "Pixar2", "logo.png", "USA");
        List<ProductionCompany> companies = new ArrayList<>();
        companies.add(company1);
        companies.add(company2);

        final ProductionCountry country1 = new ProductionCountry(1L, "us", "USA");
        final ProductionCountry country2 = new ProductionCountry(2L, "ca", "Canada");
        List<ProductionCountry> countries = new ArrayList<>();
        countries.add(country1);
        countries.add(country2);

        final SpokenLanguage language1 = new SpokenLanguage(1L, "english", "en", "english");
        final SpokenLanguage language2 = new SpokenLanguage(2L, "spanish", "es", "spanish");
        List<SpokenLanguage> languages = new ArrayList<>();
        languages.add(language1);
        languages.add(language2);

        final MovieDto dto = new MovieDto(true, "path", 30000L, "homepage", "imdb", "language", "title", "overview", 2000.5D,
                "poster.png", "1994-05-12", 5532L, 50, "status", "tagline", "title", false, 23213D, 888921L,
                companies, genres, countries, languages);

        final Movie movie = new MovieBuilder()
                .setId(1L)
                .setAdult(false)
                .setVideo(false)
                .setBackdropPath("path")
                .setBudget(11111L)
                .setHomepage("homepage")
                .setImdbId("imdb")
                .setOriginalLanguage("language")
                .setOriginalTitle("title")
                .setOverview("overview")
                .setPopularity(222D)
                .setPosterPath("poster.png")
                .setTitle(null)
                .build();

        when(repository.findById(movie.getId())).thenReturn(Optional.of(movie));
        when(repository.existsByImdbId(dto.getImdbId())).thenReturn(true);
        when(repository.existsByOverview(dto.getOverview())).thenReturn(true);
        when(repository.existsByPosterPath(dto.getPosterPath())).thenReturn(true);
        final ResponseEntity<?> expected = underTest.update(movie.getId(), dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(Movie.class);
        assertThat(expected.getBody()).asString().contains("id=1");
        assertThat(expected.getBody()).asString().contains("adult=true");
        assertThat(expected.getBody()).asString().contains("budget=30000");
        assertThat(expected.getBody()).asString().contains("title=title");
    }

    @Test
    void shouldNotUpdateDuplicatedValue() {
        final Movie movie = new MovieBuilder()
                .setId(1L)
                .setAdult(false)
                .setVideo(false)
                .setBackdropPath("path")
                .setBudget(11111L)
                .setHomepage("homepage")
                .setImdbId("imdb")
                .setOriginalLanguage("language")
                .setOriginalTitle("title")
                .setOverview("overview")
                .setPopularity(222D)
                .setPosterPath("poster.png")
                .setTitle(null)
                .build();
        final MovieDto dto = new MovieDto();
        dto.setImdbId("imdb22");

        when(repository.findById(movie.getId())).thenReturn(Optional.of(movie));
        when(repository.existsByImdbId(dto.getImdbId())).thenReturn(true);
        final ResponseEntity<?> expected = underTest.update(movie.getId(), dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(400);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("Duplicated values. Fields ImdbId, Overview and PosterPath must be unique");
    }

    @Test
    void delete() {
        final Movie movie = new MovieBuilder()
                .setId(1L)
                .setAdult(false)
                .setVideo(false)
                .setBackdropPath("path")
                .setBudget(11111L)
                .setHomepage("homepage")
                .setImdbId("imdb")
                .setOriginalLanguage("language")
                .setOriginalTitle("title")
                .setOverview("overview")
                .setPopularity(222D)
                .setPosterPath("poster.png")
                .setTitle(null)
                .build();

        when(repository.findById(movie.getId())).thenReturn(Optional.of(movie));
        final ResponseEntity<?> expected = underTest.delete(movie.getId());

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("Movie " + movie.getId() + " deleted with success");
    }
}