package com.cleverpy.moviesAPI.repositories;

import com.cleverpy.moviesAPI.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Genres' entity repository
 */
@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    boolean existsById(Long id);
    boolean existsByName(String name);
    Optional<Genre> findById(Long id);
    List<Genre> findAll();

}
