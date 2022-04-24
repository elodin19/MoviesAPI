package com.cleverpy.moviesAPI;

import com.cleverpy.moviesAPI.dto.MovieDto;
import com.cleverpy.moviesAPI.entities.Genre;
import com.cleverpy.moviesAPI.entities.ProductionCompany;
import com.cleverpy.moviesAPI.entities.ProductionCountry;
import com.cleverpy.moviesAPI.entities.SpokenLanguage;
import com.cleverpy.moviesAPI.services.movie.MovieServiceImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class MoviesApiApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(MoviesApiApplication.class, args);
		MovieServiceImpl service = context.getBean(MovieServiceImpl.class);

		final String KEY = System.getenv("MovieDBKey");

		//Get movies from theMovieDB
		HttpURLConnection connection = null;
		BufferedReader reader;
		String line;
		StringBuffer responseContent = new StringBuffer();

		for (int i = 860; i <= 910; i++){
			try{
				URL url = new URL("https://api.themoviedb.org/3/movie/" + i + 
						"?api_key=" + KEY + "&language=en-US");
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);
				
				int status = connection.getResponseCode();
				if (status > 299){
					reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
					while((line = reader.readLine()) != null)
						responseContent.append(line + "\n");
					reader.close();
				} else {
					reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					line = reader.readLine();

					JSONObject movie = new JSONObject(line);
					Boolean adult = movie.optBoolean("adult");
					String backdrop_path = movie.optString("backdrop_path");
					Long budget = movie.optLong("budget");
					String homepage = movie.optString("homepage");
					String imdb_id = movie.optString("imdb_id");
					String original_language = movie.optString("original_language");
					String original_title = movie.optString("original_title");
					String overview = movie.optString("overview");
					Double popularity = movie.optDouble("popularity");
					String poster_path = movie.optString("poster_path");
					String release_date = movie.optString("release_date");
					Long revenue = movie.optLong("revenue");
					Integer runtime = movie.optInt("runtime");
					String movieStatus = movie.optString("status");
					String tagline = movie.optString("tagline");
					String title = movie.optString("title");
					Boolean video = movie.optBoolean("video");
					Double vote_average = movie.optDouble("vote_average");
					Long vote_count = movie.optLong("vote_count");

					List<Genre> listGenres = new ArrayList<>();
					JSONArray genres = movie.getJSONArray("genres");
					for (int j = 0; j < genres.length(); j++){
						JSONObject genre = genres.getJSONObject(j);
						String name = genre.optString("name");
						listGenres.add(new Genre(null, name));
					}

					List<ProductionCompany> listCompanies = new ArrayList<>();
					JSONArray companies = movie.getJSONArray("production_companies");
					for (int j = 0; j < companies.length(); j++){
						JSONObject company = companies.getJSONObject(j);
						String name = company.optString("name");
						String logo_path = company.optString("logo_path");
						String origin_country = company.optString("origin_country");
						listCompanies.add(new ProductionCompany(null, name, logo_path, origin_country));
					}

					List<ProductionCountry> listCountries = new ArrayList<>();
					JSONArray countries = movie.getJSONArray("production_countries");
					for (int j = 0; j < countries.length(); j++){
						JSONObject country = countries.getJSONObject(j);
						String iso_3166_1 = country.optString("iso_3166_1");
						String name = country.optString("name");
						listCountries.add(new ProductionCountry(null, iso_3166_1, name));
					}

					List<SpokenLanguage> listLanguages = new ArrayList<>();
					JSONArray languages = movie.getJSONArray("spoken_languages");
					for (int j = 0; j < languages.length(); j++){
						JSONObject language = languages.getJSONObject(j);
						String english_name = language.optString("english_name");
						String iso_639_1 = language.optString("iso_639_1");
						String name = language.optString("name");
						listLanguages.add(new SpokenLanguage(null, english_name, iso_639_1, name));
					}

					MovieDto movieDto = new MovieDto(adult, backdrop_path, budget, homepage, imdb_id, original_language,
							original_title, overview, popularity, poster_path, release_date, revenue, runtime, movieStatus,
							tagline, title, video, vote_average, vote_count, listCompanies, listGenres, listCountries,
							listLanguages);

					service.create(movieDto);

					reader.close();
				}

			} catch (MalformedURLException e){
				System.err.println("Error 1: " + e.getMessage());
			} catch (IOException e){
				System.err.println("Error 2: " +e.getMessage());
			} finally {
				connection.disconnect();
			}
		}
	}

}
