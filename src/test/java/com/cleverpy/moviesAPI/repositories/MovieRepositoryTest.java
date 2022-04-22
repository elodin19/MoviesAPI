package com.cleverpy.moviesAPI.repositories;

import com.cleverpy.moviesAPI.builders.MovieBuilder;
import com.cleverpy.moviesAPI.entities.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository underTest;

    @BeforeEach
    void setUp() {
        Movie movie = new MovieBuilder()
                .setId(1L)
                .setAdult(false)
                .setBudget(1000L)
                .setImdbId("imdb")
                .setOriginalLanguage("english")
                .setOriginalTitle("Jackass")
                .setOverview("overview")
                .setPopularity(8332.2D)
                .setPosterPath("poster.jpg")
                .setReleaseDate("2010-10-10")
                .setRevenue(2000000L)
                .setTitle("Jackass")
                .setVoteAverage(9.2D)
                .setVoteCount(82312L)
                .setVideo(false)
                .build();
        underTest.save(movie);
    }

    @AfterEach
    void tearDown() {
        Movie movie = underTest.getById(1L);
        underTest.delete(movie);
    }

    @Test
    void existsById() {
        boolean expected = underTest.existsById(1L);
        assertThat(expected).isTrue();
    }

    @Test
    void doesNotExistById() {
        boolean expected = underTest.existsById(2L);
        assertThat(expected).isFalse();
    }

    @Test
    void existsByImdbId() {
        boolean expected = underTest.existsByImdbId("imdb");
        assertThat(expected).isTrue();
    }

    @Test
    void doesNotExistByImdbId() {
        boolean expected = underTest.existsByImdbId("imdb202");
        assertThat(expected).isFalse();
    }

    @Test
    void existsByOverview() {
        boolean expected = underTest.existsByOverview("overview");
        assertThat(expected).isTrue();
    }

    @Test
    void doesNotExistByOverview() {
        boolean expected = underTest.existsByOverview("Some crazy people");
        assertThat(expected).isFalse();
    }

    @Test
    void existsByPosterPath() {
        boolean expected = underTest.existsByPosterPath("poster.jpg");
        assertThat(expected).isTrue();
    }

    @Test
    void doesNotExistByPosterPath() {
        boolean expected = underTest.existsByPosterPath("poster.png");
        assertThat(expected).isFalse();
    }

    @Test
    void findById() {
        Optional<Movie> expected = underTest.findById(1L);
        assertThat(expected).isPresent();
    }

    @Test
    void doesNotFindById() {
        Optional<Movie> expected = underTest.findById(40L);
        assertThat(expected).isEmpty();
    }

    @Test
    void findAll() {
        Pageable page = PageRequest.of(0, 10);
        Page<Movie> expected = underTest.findAll(page);
        assertThat(expected.toList().size() > 0).isTrue();
    }

    @Test
    void doesNotFindAll() {
        Pageable page = PageRequest.of(5, 10);
        Page<Movie> expected = underTest.findAll(page);
        assertThat(expected.toList().size() > 0).isFalse();
    }
}