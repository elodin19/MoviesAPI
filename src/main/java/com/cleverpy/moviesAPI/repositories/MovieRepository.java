package com.cleverpy.moviesAPI.repositories;

import com.cleverpy.moviesAPI.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Movies' entity repository
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    boolean existsById(Long id);
    Optional<Movie> findById(Long id);

}
