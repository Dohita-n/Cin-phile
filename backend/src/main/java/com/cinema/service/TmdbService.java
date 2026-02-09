package com.cinema.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * Service pour interagir avec l'API TMDB
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TmdbService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.base-url}")
    private String baseUrl;

    @Value("${tmdb.api.image-base-url}")
    private String imageBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Recherche de films par titre
     */
    public Map<String, Object> searchMovies(String query, Integer page, Integer year) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/search/movie")
                .queryParam("api_key", apiKey)
                .queryParam("query", query)
                .queryParam("page", page != null ? page : 1)
                .queryParam("language", "fr-FR");

        if (year != null) {
            builder.queryParam("year", year);
        }

        String url = builder.toUriString();

        log.debug("Recherche TMDB: {} (Year: {})", query, year);
        return restTemplate.getForObject(url, Map.class);
    }

    /**
     * Récupère les détails d'un film
     */
    public Map<String, Object> getMovieDetails(Long movieId) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/movie/" + movieId)
                .queryParam("api_key", apiKey)
                .queryParam("language", "fr-FR")
                .queryParam("append_to_response", "credits,videos")
                .toUriString();

        log.debug("Récupération détails film TMDB ID: {}", movieId);
        return restTemplate.getForObject(url, Map.class);
    }

    /**
     * Récupère les films similaires
     */
    public Map<String, Object> getSimilarMovies(Long movieId, Integer page) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/movie/" + movieId + "/similar")
                .queryParam("api_key", apiKey)
                .queryParam("page", page != null ? page : 1)
                .queryParam("language", "fr-FR")
                .toUriString();

        log.debug("Récupération films similaires pour TMDB ID: {}", movieId);
        return restTemplate.getForObject(url, Map.class);
    }

    /**
     * Récupère les recommandations de films
     */
    public Map<String, Object> getMovieRecommendations(Long movieId, Integer page) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/movie/" + movieId + "/recommendations")
                .queryParam("api_key", apiKey)
                .queryParam("page", page != null ? page : 1)
                .queryParam("language", "fr-FR")
                .toUriString();

        log.debug("Récupération recommandations pour TMDB ID: {}", movieId);
        return restTemplate.getForObject(url, Map.class);
    }

    /**
     * Récupère la liste des genres
     */
    public Map<String, Object> getGenres() {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/genre/movie/list")
                .queryParam("api_key", apiKey)
                .queryParam("language", "fr-FR")
                .toUriString();

        log.debug("Récupération liste des genres");
        return restTemplate.getForObject(url, Map.class);
    }

    /**
     * Découvrir des films par genre
     */
    public Map<String, Object> discoverMoviesByGenre(Long genreId, Integer page) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/discover/movie")
                .queryParam("api_key", apiKey)
                .queryParam("with_genres", genreId)
                .queryParam("page", page != null ? page : 1)
                .queryParam("language", "fr-FR")
                .queryParam("sort_by", "popularity.desc")
                .toUriString();

        log.debug("Découverte films par genre: {}", genreId);
        return restTemplate.getForObject(url, Map.class);
    }

    /**
     * Récupère les films populaires
     */
    public Map<String, Object> getPopularMovies(Integer page) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/movie/popular")
                .queryParam("api_key", apiKey)
                .queryParam("page", page != null ? page : 1)
                .queryParam("language", "fr-FR")
                .toUriString();

        log.debug("Récupération films populaires");
        return restTemplate.getForObject(url, Map.class);
    }

    /**
     * Recherche de films avec filtres avancés
     */
    public Map<String, Object> discoverMovies(Integer year, Long genreId, String sortBy, Integer page) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/discover/movie")
                .queryParam("api_key", apiKey)
                .queryParam("page", page != null ? page : 1)
                .queryParam("language", "fr-FR");

        if (year != null) {
            builder.queryParam("year", year);
        }
        if (genreId != null) {
            builder.queryParam("with_genres", genreId);
        }
        if (sortBy != null) {
            builder.queryParam("sort_by", sortBy);
        } else {
            builder.queryParam("sort_by", "popularity.desc");
        }

        String url = builder.toUriString();
        log.debug("Découverte films avec filtres");
        return restTemplate.getForObject(url, Map.class);
    }

    /**
     * Recherche une personne (acteur ou réalisateur) par nom
     */
    public Map<String, Object> searchPerson(String name, Integer page) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/search/person")
                .queryParam("api_key", apiKey)
                .queryParam("query", name)
                .queryParam("page", page != null ? page : 1)
                .queryParam("language", "fr-FR")
                .toUriString();

        log.debug("Recherche personne TMDB: {}", name);
        return restTemplate.getForObject(url, Map.class);
    }

    /**
     * Récupère les crédits de films d'une personne (acteur ou réalisateur)
     */
    public Map<String, Object> getPersonMovieCredits(Long personId) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/person/" + personId + "/movie_credits")
                .queryParam("api_key", apiKey)
                .queryParam("language", "fr-FR")
                .toUriString();

        log.debug("Récupération crédits pour personne TMDB ID: {}", personId);
        return restTemplate.getForObject(url, Map.class);
    }

    /**
     * Construit l'URL complète d'une image
     */
    public String getImageUrl(String path, String size) {
        if (path == null)
            return null;
        return imageBaseUrl + "/" + size + path;
    }
}
