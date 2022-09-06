package com.gfttraining.tmdb.controller;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.gfttraining.tmdb.h2utils.H2JDBCUtils;
import com.gfttraining.tmdb.model.User_movie;
import com.gfttraining.tmdb.sqlquerys.sqlquerys;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
@RequestMapping (value = "api")
public class TMDBController {
	
	private sqlquerys sqlquerys=new sqlquerys();
	
	
	public ResponseEntity<String> inicializator(String urlString) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlZmJiZDdhYmNjZTkxYmRkNjI4ZWY1NjlkNDAwYzhlOSIsInN1YiI6IjYzMTVhYzQ2ZmFiM2ZhMDA4NGMyMWQ1MCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.Z7IAUwTmrGOfFwCMOkFBVBKWBGZV22pSroBUPDVIfcg");
		HttpEntity request = new HttpEntity(headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(urlString, HttpMethod.GET,request,String.class);
		return responseEntity;
	}
	
	
	@GetMapping("movie/toprated")
	public ResponseEntity<String> getTopRated() {
		String urlString="https://api.themoviedb.org/3/movie/top_rated?language=es-ES";
		
		return inicializator(urlString);
	}
	
	@GetMapping("genre/movie/list")
	public ResponseEntity<String> getGenreList() {
		String urlString="https://api.themoviedb.org/3/genre/movie/list?language=es-ES";
		
		return inicializator(urlString);
	}
	
	@GetMapping("movie/popular")
	public ResponseEntity<String> getPopulars() {
		String urlString="https://api.themoviedb.org/3/movie/popular?language=es-ES";
		
		return inicializator(urlString);
	}
	
	@GetMapping("/movie/{movie_id}")
    public ResponseEntity<JsonObject> getMovie(@PathVariable int movie_id) throws SQLException {
        String urlString="https://api.themoviedb.org/3/movie/"+movie_id+"?language=es-ES";
        
        ResponseEntity<String> responseEntity = inicializator(urlString);
        String jsonResponse = responseEntity.getBody();
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
        String currentUserName = authentication.getName();
        int userId = sqlquerys.retrieveUserId(currentUserName);
        User_movie userMovie = sqlquerys.getUser_movieByID(movie_id, userId);
        
        if(userMovie == null) {
             String json = jsonResponse.substring(0, jsonResponse.length()-1).concat(",\"favourite\": \"false\", \"personal_rating\":\"null\", \"notes\": \"null\"}");
        
            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
            
            return responseEntity.ok(jsonObject);
        }
        else {
            String json = jsonResponse.substring(0, jsonResponse.length()-1).concat(",\"favourite\": \"" + userMovie.isFavourite() + "\", \"personal_rating\":\"" + userMovie.getPersonal_rating() + "\", \"notes\": \"" + userMovie.getNotes() + "\"}");
            
            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
            return responseEntity.ok(jsonObject);
        }
        
         }
        return responseEntity.ok(new JsonParser().parse("{}").getAsJsonObject());
    }
	
	@PatchMapping("/movie/{movie_id}")
    public ResponseEntity<JsonObject> postMovie(@PathVariable int movie_id, @RequestBody User_movie user_movie)
            throws SQLException {
		String urlString="https://api.themoviedb.org/3/movie/"+movie_id+"?language=es-ES";



       String currentUserName = "";
        int userId = 0;



       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {



           currentUserName = authentication.getName();
            System.out.println(currentUserName);
            userId = sqlquerys.retrieveUserId(currentUserName);
            user_movie.setUserid(userId);
            try {



               User_movie user = sqlquerys.getUser_movieByID(movie_id, userId);



               if (user == null) {
            	   sqlquerys.insertRecord(user_movie, movie_id,userId);
                } else {
                	sqlquerys.updateRecord(user_movie, movie_id);
                }



           } catch (SQLException e) {



               e.printStackTrace();
            }
        }


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlZmJiZDdhYmNjZTkxYmRkNjI4ZWY1NjlkNDAwYzhlOSIsInN1YiI6IjYzMTVhYzQ2ZmFiM2ZhMDA4NGMyMWQ1MCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.Z7IAUwTmrGOfFwCMOkFBVBKWBGZV22pSroBUPDVIfcg");
		HttpEntity request = new HttpEntity(headers);
       User_movie userMovie = sqlquerys.getUser_movieByID(movie_id, userId);
        ResponseEntity<String> responseEntity = restTemplate.exchange(urlString, HttpMethod.GET, request,
                String.class);
        String jsonResponse = responseEntity.getBody();



       if (userMovie == null) {
            String json = jsonResponse.substring(0, jsonResponse.length() - 1)
                    .concat(",\"favourite\": \"false\", \"personal_rating\":\"null\", \"notes\": \"null\"}");



           JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();



           return responseEntity.ok(jsonObject);
        } else {
            String json = jsonResponse.substring(0, jsonResponse.length() - 1)
                    .concat(",\"favourite\": \"" + userMovie.isFavourite() + "\", \"personal_rating\":\""
                            + userMovie.getPersonal_rating() + "\", \"notes\": \"" + userMovie.getNotes() + "\"}");



           JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
            return responseEntity.ok(jsonObject);
        }



   }
	
	
	@GetMapping("/movie/{movie_id}/credits")
	public ResponseEntity<String> getMovieCredits(@PathVariable String movie_id) {
		String urlString="https://api.themoviedb.org/3/movie/"+movie_id+"/credits?language=es-ES";
		
		return inicializator(urlString);
	}
	
	@GetMapping("/movie/{movie_id}/images")
	public ResponseEntity<String> getMovieImages(@PathVariable String movie_id) {
		String urlString="https://api.themoviedb.org/3/movie/"+movie_id+"/images";
		
		return inicializator(urlString);
	}
	
	@GetMapping("/movie/{movie_id}/keywords")
	public ResponseEntity<String> getMovieKeywords(@PathVariable String movie_id) {
		String urlString="https://api.themoviedb.org/3/movie/"+movie_id+"/keywords?language=es-ES";
		
		return inicializator(urlString);
	}
	
	@GetMapping("/movie/{movie_id}/recommendations")
	public ResponseEntity<String> getMovieRecommendations(@PathVariable String movie_id) {
		String urlString="https://api.themoviedb.org/3/movie/"+movie_id+"/recommendations?language=es-ES";
		
		return inicializator(urlString);
	}
	
	@GetMapping("/movie/{movie_id}/similar")
	public ResponseEntity<String> getMovieSimilar(@PathVariable String movie_id) {
		String urlString="https://api.themoviedb.org/3/movie/"+movie_id+"/similar?language=es-ES";
		return inicializator(urlString);
	}

	
	
	
}
