package com.cleverpy.moviesAPI.repositories;

import com.cleverpy.moviesAPI.entities.User;
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
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @BeforeEach
    void setUp() {
        final User user = new User(3L, "test@gmail.com", "testing", "1234", null);
        underTest.save(user);
    }

    @AfterEach
    void tearDown() {
        final User user = underTest.getById(1L);
        underTest.delete(user);
    }

    @Test
    void findByUsername() {
        final Optional<User> expected = underTest.findByUsername("testing");
        assertThat(expected).isPresent();
    }

    @Test
    void doesNotFindByUsername() {
        final Optional<User> expected = underTest.findByUsername("anyone");
        assertThat(expected).isEmpty();
    }

    @Test
    void findByEmail() {
        final Optional<User> expected = underTest.findByEmail("test@gmail.com");
        assertThat(expected).isPresent();
    }

    @Test
    void doesNotFindByEmail() {
        final Optional<User> expected = underTest.findByEmail("test@hotmail.com");
        assertThat(expected).isEmpty();
    }

    @Test
    void findAll() {
        final Pageable page = PageRequest.of(0, 10);
        final Page<User> expected = underTest.findAll(page);
        assertThat(expected.toList().size() > 0).isTrue();
    }

    @Test
    void doesNotFindAll() {
        final Pageable page = PageRequest.of(1, 10);
        final Page<User> expected = underTest.findAll(page);
        assertThat(expected.toList().size() > 0).isFalse();
    }

    @Test
    void existsByUsername() {
        final boolean expected = underTest.existsByUsername("testing");
        assertThat(expected).isTrue();
    }

    @Test
    void doesNotExistByUsername() {
        final boolean expected = underTest.existsByUsername("anyone");
        assertThat(expected).isFalse();
    }

    @Test
    void existsByEmail() {
        final boolean expected = underTest.existsByEmail("test@gmail.com");
        assertThat(expected).isTrue();
    }

    @Test
    void doesNotExistByEmail() {
        final boolean expected = underTest.existsByEmail("test@hotmail.com");
        assertThat(expected).isFalse();
    }
}