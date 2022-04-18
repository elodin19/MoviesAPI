package com.cleverpy.moviesAPI.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity that manages the Spoken Languages in the database
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "spoken_languages")
public class SpokenLanguage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "english_name", nullable = false)
    private String englishName;

    @Column(nullable = false, unique = true)
    private String iso_639_1;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "spokenLanguages")
    private List<Movie> movies = new ArrayList<>();
}
