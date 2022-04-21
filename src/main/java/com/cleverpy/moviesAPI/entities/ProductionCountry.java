package com.cleverpy.moviesAPI.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Entity that manages the Production Countries in the database
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "production_countries")
public class ProductionCountry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 2)
    private String iso;

    @Column(nullable = false, unique = true)
    private String name;

}
