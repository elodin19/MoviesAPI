package com.cleverpy.moviesAPI.repositories;

import com.cleverpy.moviesAPI.entities.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Movies' entity repository
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    boolean existsById(Long id);
    boolean existsByImdbId(String ImdbId);
    boolean existsByOverview(String overview);
    boolean existsByPosterPath(String posterPath);
    boolean existsByTitle(String title);
    Optional<Movie> findById(Long id);
    Page<Movie> findAll(Pageable page);

}
