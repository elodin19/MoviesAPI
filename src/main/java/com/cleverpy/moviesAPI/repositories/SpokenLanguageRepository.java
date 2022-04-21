package com.cleverpy.moviesAPI.repositories;

import com.cleverpy.moviesAPI.entities.SpokenLanguage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spoken Languages' entity repository
 */
@Repository
public interface SpokenLanguageRepository extends JpaRepository<SpokenLanguage, Long> {

    boolean existsById(Long id);
    boolean existsByName(String name);
    boolean existsByIso(String iso);
    Optional<SpokenLanguage> findById(Long id);
    Optional<SpokenLanguage> findByName(String name);
    Page<SpokenLanguage> findAll(Pageable page);
}
