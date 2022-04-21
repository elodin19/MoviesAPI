package com.cleverpy.moviesAPI.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity that manages the Movie in the database
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean adult;

    @Column(name = "backdrop_path")
    private String backdropPath;

    @Column(nullable = false)
    private Long budget;

    private String homepage;

    @Column(unique = true)
    private String imdbId;

    @Column(name = "original_language", nullable = false)
    private String originalLanguage;

    @Column(name = "original_title", nullable = false)
    private String originalTitle;

    @Column(unique = true, nullable = false, length = 1024)
    private String overview;

    @Column(nullable = false)
    private Double popularity;

    @Column(name = "poster_path", unique = true)
    private String posterPath;

    @Column(name = "release_date", nullable = false)
    private String releaseDate;

    @Column(nullable = false)
    private Long revenue;

    private Integer runtime;

    private String status;

    private String tagline;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Boolean video;

    @Column(name = "vote_average", nullable = false)
    private Double voteAverage;

    @Column(name = "vote_count", nullable = false)
    private Long voteCount;

    @ManyToMany
    @JoinTable(name = "MOVIES_PRODUCTION_COMPANIES",
            joinColumns = {
                    @JoinColumn(name = "MOVIE_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "PRODUCTION_COMPANY_ID") })
    private List<ProductionCompany> productionCompanies = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "MOVIES_GENRES",
            joinColumns = {
                    @JoinColumn(name = "MOVIE_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "GENRE_ID") })
    private List<Genre> genres = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "MOVIES_PRODUCTION_COUNTRIES",
            joinColumns = {
                    @JoinColumn(name = "MOVIE_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "PRODUCTION_COUNTRY_ID") })
    private List<ProductionCountry> productionCountries = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "MOVIES_SPOKEN_LANGUAGES",
            joinColumns = {
                    @JoinColumn(name = "MOVIE_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "SPOKEN_LANGUAGE_ID") })
    private List<SpokenLanguage> spokenLanguages = new ArrayList<>();
}
