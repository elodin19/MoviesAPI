package com.cleverpy.moviesAPI.repositories;

import com.cleverpy.moviesAPI.entities.ProductionCompany;
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
class ProductionCompanyRepositoryTest {

    @Autowired
    private ProductionCompanyRepository underTest;

    @BeforeEach
    void setUp(){
        final ProductionCompany company = new ProductionCompany(1L, "TestingCompany", "logo.png", "BR");
        underTest.save(company);
    }

    @AfterEach
    void tearDown(){
        final ProductionCompany company = underTest.getById(1L);
        underTest.delete(company);
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
        final boolean expected = underTest.existsByName("TestingCompany");
        assertThat(expected).isTrue();
    }

    @Test
    void doesNotExistByName() {
        final boolean expected = underTest.existsByName("Pixar");
        assertThat(expected).isFalse();
    }

    @Test
    void existsByLogoPath() {
        final boolean expected = underTest.existsByLogoPath("logo.png");
        assertThat(expected).isTrue();
    }

    @Test
    void DoesNotExistByLogoPath() {
        final boolean expected = underTest.existsByLogoPath("logo2.png");
        assertThat(expected).isFalse();
    }


    @Test
    void findById() {
        final Optional<ProductionCompany> expected = underTest.findById(1L);
        assertThat(expected).isPresent();
    }

    @Test
    void doesNotFindById() {
        final Optional<ProductionCompany> expected = underTest.findById(2L);
        assertThat(expected).isEmpty();
    }

    @Test
    void findByName() {
        final Optional<ProductionCompany> expected = underTest.findByName("TestingCompany");
        assertThat(expected).isPresent();
    }

    @Test
    void doesNotFindByName() {
        final Optional<ProductionCompany> expected = underTest.findByName("Pixar");
        assertThat(expected).isEmpty();
    }

    @Test
    void findAll() {
        final Pageable page = PageRequest.of(0, 10);
        final Page<ProductionCompany> expected = underTest.findAll(page);
        assertThat(expected.toList().size() > 0).isTrue();
    }

    @Test
    void doesNotFindAll() {
        final Pageable page = PageRequest.of(2, 10);
        final Page<ProductionCompany> expected = underTest.findAll(page);
        assertThat(expected.toList().size() > 0).isFalse();
    }
}