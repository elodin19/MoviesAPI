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
        ProductionCompany company = new ProductionCompany(1L, "TestingCompany", "logo.png", "BR");
        underTest.save(company);
    }

    @AfterEach
    void tearDown(){
        ProductionCompany company = underTest.getById(1L);
        underTest.delete(company);
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
        boolean expected = underTest.existsByName("TestingCompany");
        assertThat(expected).isTrue();
    }

    @Test
    void doesNotExistByName() {
        boolean expected = underTest.existsByName("Pixar");
        assertThat(expected).isFalse();
    }

    @Test
    void existsByLogoPath() {
        boolean expected = underTest.existsByLogoPath("logo.png");
        assertThat(expected).isTrue();
    }

    @Test
    void DoesNotExistByLogoPath() {
        boolean expected = underTest.existsByLogoPath("logo2.png");
        assertThat(expected).isFalse();
    }


    @Test
    void findById() {
        Optional<ProductionCompany> expected = underTest.findById(1L);
        assertThat(expected).isPresent();
    }

    @Test
    void doesNotFindById() {
        Optional<ProductionCompany> expected = underTest.findById(2L);
        assertThat(expected).isEmpty();
    }

    @Test
    void findByName() {
        Optional<ProductionCompany> expected = underTest.findByName("TestingCompany");
        assertThat(expected).isPresent();
    }

    @Test
    void doesNotFindByName() {
        Optional<ProductionCompany> expected = underTest.findByName("Pixar");
        assertThat(expected).isEmpty();
    }

    @Test
    void findAll() {
        Pageable page = PageRequest.of(0, 10);
        Page<ProductionCompany> expected = underTest.findAll(page);
        assertThat(expected.toList().size() > 0).isTrue();
    }

    @Test
    void doesNotFindAll() {
        Pageable page = PageRequest.of(2, 10);
        Page<ProductionCompany> expected = underTest.findAll(page);
        assertThat(expected.toList().size() > 0).isFalse();
    }
}