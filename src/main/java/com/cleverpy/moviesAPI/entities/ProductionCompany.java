package com.cleverpy.moviesAPI.entities;

import com.cleverpy.moviesAPI.dto.productionCompany.ProductionCompanyDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "logo_path", unique = true)
    private String logoPath;

    @Column(name = "origin_country", nullable = false, length = 2)
    private String originCountry;

    @ManyToMany(mappedBy = "productionCompanies")
    private List<Movie> movies = new ArrayList<>();

    public ProductionCompanyDto getDto(){
        return new ProductionCompanyDto(name, logoPath, originCountry);
    }
}
