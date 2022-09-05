package com.gfttraining.tmdb.controller;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping (value = "api")
public class TMDBController {
	
	
	
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
	public ResponseEntity<String> getMovie(@PathVariable String movie_id) {
		String urlString="https://api.themoviedb.org/3/movie/"+movie_id+"?language=es-ES";
		
		return inicializator(urlString);
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
