package com.cleverpy.moviesAPI.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Entity that manages the Production Companies in the database
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "production_companies")
public class ProductionCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "logo_path")
    private String logoPath;

    @Column(name = "origin_country", length = 2)
    private String originCountry;
}
