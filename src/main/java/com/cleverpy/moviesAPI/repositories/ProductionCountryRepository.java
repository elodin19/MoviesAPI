package com.cleverpy.moviesAPI.repositories;

import com.cleverpy.moviesAPI.entities.ProductionCountry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Production Countries' entity repository
 */
@Repository
public interface ProductionCountryRepository extends JpaRepository<ProductionCountry, Long> {

    boolean existsById(Long id);
    boolean existsByName(String name);
    boolean existsByIso(String iso);
    Optional<ProductionCountry> findById(Long id);
    Optional<ProductionCountry> findByName(String name);
    Page<ProductionCountry> findAll(Pageable page);

}
