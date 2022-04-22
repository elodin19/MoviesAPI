package com.cleverpy.moviesAPI.repositories;

import com.cleverpy.moviesAPI.entities.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository underTest;

    @BeforeEach
    void setUp() {
        Role role = new Role(1L, "MANAGER");
        underTest.save(role);
    }

    @AfterEach
    void tearDown() {
        Role role = underTest.getById(1L);
        underTest.delete(role);
    }

    @Test
    void findByName() {
        Optional<Role> expected = underTest.findByName("MANAGER");
        assertThat(expected).isPresent();
    }

    @Test
    void doesNotFindByName() {
        Optional<Role> expected = underTest.findByName("CLIENT");
        assertThat(expected).isEmpty();
    }
}