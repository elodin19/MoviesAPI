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
        Genre genre = new Genre(1L, "Horror");
        underTest.save(genre);
    }

    @AfterEach
    void tearDown(){
        Genre genre = underTest.getById(1L);
        underTest.delete(genre);
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
    void existsByName() {
        boolean expected = underTest.existsByName("Horror");
        assertThat(expected).isTrue();
    }

    @Test
    void doesNotExistByName() {
        boolean expected = underTest.existsByName("Comedy");
        assertThat(expected).isFalse();
    }

    @Test
    void findById() {
        Optional<Genre> expected = underTest.findById(1L);
        assertThat(expected).isPresent();
    }

    @Test
    void doesNotFindById() {
        Optional<Genre> expected = underTest.findById(2L);
        assertThat(expected).isEmpty();
    }

    @Test
    void findByName() {
        Optional<Genre> expected = underTest.findByName("Horror");
        assertThat(expected).isPresent();
    }

    @Test
    void doesNotFindByName() {
        Optional<Genre> expected = underTest.findByName("Comedy");
        assertThat(expected).isEmpty();
    }

    @Test
    void findAll() {
        Pageable page = PageRequest.of(0, 10);
        Page<Genre> expected = underTest.findAll(page);
        assertThat(expected.toList().size() > 0).isTrue();
    }

    @Test
    void doesNotFindAll() {
        Pageable page = PageRequest.of(2, 10);
        Page<Genre> expected = underTest.findAll(page);
        assertThat(expected.toList().size() > 0).isFalse();
    }
}