package com.cleverpy.moviesAPI.entities;

import com.cleverpy.moviesAPI.dto.productionCountry.ProductionCountryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false, unique = true)
    private String iso;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "productionCountries")
    private List<Movie> movies = new ArrayList<>();

    public ProductionCountryDto getDto(){
        return new ProductionCountryDto(id, iso, name);
    }
}
