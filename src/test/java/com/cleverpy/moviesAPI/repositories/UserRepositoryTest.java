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
        User user = new User(3L, "test@gmail.com", "testing", "1234", null);
        underTest.save(user);
    }

    @AfterEach
    void tearDown() {
        User user = underTest.getById(1L);
        underTest.delete(user);
    }

    @Test
    void findByUsername() {
        Optional<User> expected = underTest.findByUsername("testing");
        assertThat(expected).isPresent();
    }

    @Test
    void doesNotFindByUsername() {
        Optional<User> expected = underTest.findByUsername("anyone");
        assertThat(expected).isEmpty();
    }

    @Test
    void findByEmail() {
        Optional<User> expected = underTest.findByEmail("test@gmail.com");
        assertThat(expected).isPresent();
    }

    @Test
    void doesNotFindByEmail() {
        Optional<User> expected = underTest.findByEmail("test@hotmail.com");
        assertThat(expected).isEmpty();
    }

    @Test
    void findAll() {
        Pageable page = PageRequest.of(0, 10);
        Page<User> expected = underTest.findAll(page);
        assertThat(expected.toList().size() > 0).isTrue();
    }

    @Test
    void doesNotFindAll() {
        Pageable page = PageRequest.of(1, 10);
        Page<User> expected = underTest.findAll(page);
        assertThat(expected.toList().size() > 0).isFalse();
    }

    @Test
    void existsByUsername() {
        boolean expected = underTest.existsByUsername("testing");
        assertThat(expected).isTrue();
    }

    @Test
    void doesNotExistByUsername() {
        boolean expected = underTest.existsByUsername("anyone");
        assertThat(expected).isFalse();
    }

    @Test
    void existsByEmail() {
        boolean expected = underTest.existsByEmail("test@gmail.com");
        assertThat(expected).isTrue();
    }

    @Test
    void doesNotExistByEmail() {
        boolean expected = underTest.existsByEmail("test@hotmail.com");
        assertThat(expected).isFalse();
    }
}