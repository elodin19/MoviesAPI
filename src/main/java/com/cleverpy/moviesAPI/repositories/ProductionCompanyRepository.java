package com.cleverpy.moviesAPI.repositories;

import com.cleverpy.moviesAPI.entities.ProductionCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Answers' entity repository
 */
@Repository
public interface ProductionCompanyRepository extends JpaRepository<ProductionCompany, Long> {

    boolean existsById(Long id);
    boolean existsByName(String name);
    boolean existsByLogoPath(String logoPath);
    Optional<ProductionCompany> findById(Long id);
    Optional<ProductionCompany> findByName(String name);
    Page<ProductionCompany> findAll(Pageable page);

}
