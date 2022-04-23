package com.cleverpy.moviesAPI.repositories;

import com.cleverpy.moviesAPI.entities.Genre;
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
class GenreRepositoryTest {

    @Autowired
    private GenreRepository underTest;

    @BeforeEach
    void setUp(){
        final Genre genre = new Genre(1L, "Horror");
        underTest.save(genre);
    }

    @AfterEach
    void tearDown(){
        final Genre genre = underTest.getById(1L);
        underTest.delete(genre);
    }

    @Test
    void existsById() {
        final boolean expected = underTest.existsById(1L);
        assertThat(expected).isTrue();
    }

    @Test
    void doesNotExistById() {
        final boolean expected = underTest.existsById(2L);
        assertThat(expected).isFalse();
    }

    @Test
    void existsByName() {
        final boolean expected = underTest.existsByName("Horror");
        assertThat(expected).isTrue();
    }

    @Test
    void doesNotExistByName() {
        final boolean expected = underTest.existsByName("Comedy");
        assertThat(expected).isFalse();
    }

    @Test
    void findById() {
        final Optional<Genre> expected = underTest.findById(1L);
        assertThat(expected).isPresent();
    }

    @Test
    void doesNotFindById() {
        final Optional<Genre> expected = underTest.findById(2L);
        assertThat(expected).isEmpty();
    }

    @Test
    void findByName() {
        final Optional<Genre> expected = underTest.findByName("Horror");
        assertThat(expected).isPresent();
    }

    @Test
    void doesNotFindByName() {
        final Optional<Genre> expected = underTest.findByName("Comedy");
        assertThat(expected).isEmpty();
    }

    @Test
    void findAll() {
        final Pageable page = PageRequest.of(0, 10);
        final Page<Genre> expected = underTest.findAll(page);
        assertThat(expected.toList().size() > 0).isTrue();
    }

    @Test
    void doesNotFindAll() {
        final Pageable page = PageRequest.of(2, 10);
        final Page<Genre> expected = underTest.findAll(page);
        assertThat(expected.toList().size() > 0).isFalse();
    }
}