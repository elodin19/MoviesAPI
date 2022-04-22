package com.cleverpy.moviesAPI.repositories;

import com.cleverpy.moviesAPI.entities.ProductionCountry;
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
class ProductionCountryRepositoryTest {

    @Autowired
    private ProductionCountryRepository underTest;

    @BeforeEach
    void setUp() {
        ProductionCountry country = new ProductionCountry(1L, "br", "Brasil");
        underTest.save(country);
    }

    @AfterEach
    void tearDown() {
        ProductionCountry country = underTest.getById(1L);
        underTest.delete(country);
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
        boolean expected = underTest.existsByName("China");
        assertThat(expected).isFalse();
    }

    @Test
    void existsByIso() {
        boolean expected = underTest.existsByIso("br");
        assertThat(expected).isTrue();
    }

    @Test
    void doesNotExistByIso() {
        boolean expected = underTest.existsByIso("ch");
        assertThat(expected).isFalse();
    }

    @Test
    void findById() {
        Optional<ProductionCountry> expected = underTest.findById(1L);
        assertThat(expected).isPresent();
    }

    @Test
    void doesNotFindById() {
        Optional<ProductionCountry> expected = underTest.findById(2L);
        assertThat(expected).isEmpty();
    }

    @Test
    void findByName() {
        Optional<ProductionCountry> expected = underTest.findByName("Brasil");
        assertThat(expected).isPresent();
    }

    @Test
    void doesNotFindByName() {
        Optional<ProductionCountry> expected = underTest.findByName("China");
        assertThat(expected).isEmpty();
    }

    @Test
    void findAll() {
        Pageable page = PageRequest.of(0, 10);
        Page<ProductionCountry> expected = underTest.findAll(page);
        assertThat(expected.toList().size() > 0).isTrue();
    }

    @Test
    void doesNotFindAll() {
        Pageable page = PageRequest.of(2, 10);
        Page<ProductionCountry> expected = underTest.findAll(page);
        assertThat(expected.toList().size() > 0).isFalse();
    }
}