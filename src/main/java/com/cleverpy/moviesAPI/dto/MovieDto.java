package com.cleverpy.moviesAPI.dto;

import com.cleverpy.moviesAPI.entities.Genre;
import com.cleverpy.moviesAPI.entities.ProductionCompany;
import com.cleverpy.moviesAPI.entities.ProductionCountry;
import com.cleverpy.moviesAPI.entities.SpokenLanguage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Dto with the data needed for the CRUD operations of the Movie
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {

    private Long id;

    @NotNull
    private Boolean adult;

    private String backdropPath;

    @NotNull
    private Long budget;

    private String homepage;

    private String imdbId;

    @NotBlank(message = "Original Language is mandatory")
    private String originalLanguage;

    @NotBlank(message = "Original Title is mandatory")
    private String originalTitle;

    @NotBlank(message = "Overview is mandatory")
    private String overview;

    @NotNull
    private Double popularity;

    private String posterPath;

    @NotBlank(message = "Release Date is mandatory")
    private String releaseDate;

    @NotNull
    private Long revenue;

    private Integer runtime;

    private String status;

    private String tagline;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotNull
    private Boolean video;

    @NotNull
    private Double voteAverage;

    @NotNull
    private Long voteCount;

    private List<ProductionCompany> productionCompanies;

    private List<Genre> genres;

    private List<ProductionCountry> productionCountries;

    private List<SpokenLanguage> spokenLanguages;
}
