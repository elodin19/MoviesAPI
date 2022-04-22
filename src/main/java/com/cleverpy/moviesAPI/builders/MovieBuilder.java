package com.cleverpy.moviesAPI.builders;

import com.cleverpy.moviesAPI.entities.*;

import java.util.List;

/**
 * Class responsible for the movie builder
 */
public class MovieBuilder {

    Movie movie;

    //Constructor
    public MovieBuilder(){
        movie = new Movie();
    }

    //Setters
    public MovieBuilder setId(Long id){
        movie.setId(id);
        return this;
    }

    public MovieBuilder setAdult(Boolean adult){
        movie.setAdult(adult);
        return this;
    }

    public MovieBuilder setBackdropPath(String backdropPath){
        movie.setBackdropPath(backdropPath);
        return this;
    }

    public MovieBuilder setBudget(Long budget){
        movie.setBudget(budget);
        return this;
    }

    public MovieBuilder setHomepage(String homepage){
        movie.setHomepage(homepage);
        return this;
    }

    public MovieBuilder setImdbId(String imdbId){
        movie.setImdbId(imdbId);
        return this;
    }

    public MovieBuilder setOriginalLanguage(String originalLanguage){
        movie.setOriginalLanguage(originalLanguage);
        return this;
    }

    public MovieBuilder setOriginalTitle(String originalTitle){
        movie.setOriginalTitle(originalTitle);
        return this;
    }

    public MovieBuilder setOverview(String overview){
        movie.setOverview(overview);
        return this;
    }

    public MovieBuilder setPopularity(Double popularity){
        movie.setPopularity(popularity);
        return this;
    }

    public MovieBuilder setPosterPath(String posterPath){
        movie.setPosterPath(posterPath);
        return this;
    }

    public MovieBuilder setReleaseDate(String releaseDate){
        movie.setReleaseDate(releaseDate);
        return this;
    }

    public MovieBuilder setRevenue(Long revenue){
        movie.setRevenue(revenue);
        return this;
    }

    public MovieBuilder setRuntime(Integer runtime){
        movie.setRuntime(runtime);
        return this;
    }

    public MovieBuilder setStatus(String status){
        movie.setStatus(status);
        return this;
    }

    public MovieBuilder setTagline(String tagline){
        movie.setTagline(tagline);
        return this;
    }

    public MovieBuilder setTitle(String title){
        movie.setTitle(title);
        return this;
    }

    public MovieBuilder setVideo(Boolean video){
        movie.setVideo(video);
        return this;
    }

    public MovieBuilder setVoteAverage(Double voteAverage){
        movie.setVoteAverage(voteAverage);
        return this;
    }

    public MovieBuilder setVoteCount(Long voteCount){
        movie.setVoteCount(voteCount);
        return this;
    }

    public MovieBuilder setProductionCompanies(List<ProductionCompany> productionCompanies){
        movie.setProductionCompanies(productionCompanies);
        return this;
    }

    public MovieBuilder setGenres(List<Genre> genres){
        movie.setGenres(genres);
        return this;
    }

    public MovieBuilder setProductionCountries(List<ProductionCountry> productionCountries){
        movie.setProductionCountries(productionCountries);
        return this;
    }

    public MovieBuilder setSpokenLanguages(List<SpokenLanguage> spokenLanguages){
        movie.setSpokenLanguages(spokenLanguages);
        return this;
    }

    //Builder
    public Movie build(){
        return this.movie;
    }
}
