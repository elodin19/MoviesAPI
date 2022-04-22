package com.cleverpy.moviesAPI.repositories;

import com.cleverpy.moviesAPI.entities.SpokenLanguage;
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
class SpokenLanguageRepositoryTest {

    @Autowired
    private SpokenLanguageRepository underTest;

    @BeforeEach
    void setUp() {
        SpokenLanguage language = new SpokenLanguage(1L, "Portuguese", "pt-br", "Brasil");
        underTest.save(language);
    }

    @AfterEach
    void tearDown() {
        SpokenLanguage language = underTest.getById(1L);
        underTest.delete(language);
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
        boolean expected = underTest.existsByName("Brasil");
        assertThat(expected).isTrue();
    }

    @Test
    void doesNotExistByName() {
        boolean expected = underTest.existsByName("Klingon");
        assertThat(expected).isFalse();
    }

    @Test
    void existsByIso() {
        boolean expected = underTest.existsByIso("pt-br");
        assertThat(expected).isTrue();
    }

    @Test
    void doesNotExistByIso() {
        boolean expected = underTest.existsByIso("kl");
        assertThat(expected).isFalse();
    }

    @Test
    void findById() {
        Optional<SpokenLanguage> expected = underTest.findById(1L);
        assertThat(expected).isPresent();
    }

    @Test
    void doesNotFindById() {
        Optional<SpokenLanguage> expected = underTest.findById(2L);
        assertThat(expected).isEmpty();
    }

    @Test
    void findByName() {
        Optional<SpokenLanguage> expected = underTest.findByName("Brasil");
        assertThat(expected).isPresent();
    }

    @Test
    void doesNotFindByName() {
        Optional<SpokenLanguage> expected = underTest.findByName("Klingon");
        assertThat(expected).isEmpty();
    }

    @Test
    void findAll() {
        Pageable page = PageRequest.of(0, 10);
        Page<SpokenLanguage> expected = underTest.findAll(page);
        assertThat(expected.toList().size() > 0).isTrue();
    }

    @Test
    void doesNotFindAll() {
        Pageable page = PageRequest.of(2, 10);
        Page<SpokenLanguage> expected = underTest.findAll(page);
        assertThat(expected.toList().size() > 0).isFalse();
    }
}