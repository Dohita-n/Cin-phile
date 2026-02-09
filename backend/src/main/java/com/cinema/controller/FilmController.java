package com.cinema.controller;

import com.cinema.model.Genre;
import com.cinema.service.FilmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller pour la gestion des films
 */
@RestController
@RequestMapping("/api/films")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Films", description = "Endpoints pour la recherche et la gestion des films")
public class FilmController {

    private final FilmService filmService;

    /**
     * Recherche de films avec filtres
     */
    @GetMapping
    @Operation(summary = "Rechercher des films", description = "Recherche de films avec filtres multiples")
    public ResponseEntity<Map<String, Object>> searchFilms(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long genre,
            @RequestParam(required = false) Integer annee,
            @RequestParam(required = false) Long utilisateurId,
            @RequestParam(defaultValue = "1") Integer page) {

        Map<String, Object> results = filmService.searchFilms(query, genre, annee, utilisateurId, page);
        return ResponseEntity.ok(results);
    }

    /**
     * Récupère les détails d'un film
     */
    @GetMapping("/{id}")
    @Operation(summary = "Détails d'un film", description = "Récupère les détails complets d'un film")
    public ResponseEntity<Map<String, Object>> getFilmDetails(
            @PathVariable Long id,
            @RequestParam(required = false) Long utilisateurId) {

        Map<String, Object> details = filmService.getFilmDetails(id, utilisateurId);
        return ResponseEntity.ok(details);
    }

    /**
     * Récupère les films populaires
     */
    @GetMapping("/populaires")
    @Operation(summary = "Films populaires", description = "Récupère les films populaires du moment")
    public ResponseEntity<Map<String, Object>> getPopularFilms(
            @RequestParam(defaultValue = "1") Integer page) {

        Map<String, Object> results = filmService.getPopularFilms(page);
        return ResponseEntity.ok(results);
    }

    /**
     * Récupère la liste des genres
     */
    @GetMapping("/genres")
    @Operation(summary = "Liste des genres", description = "Récupère tous les genres de films")
    public ResponseEntity<List<Genre>> getGenres() {
        List<Genre> genres = filmService.getGenres();
        return ResponseEntity.ok(genres);
    }

    /**
     * Recherche de films par acteur
     */
    @GetMapping("/recherche/acteur")
    @Operation(summary = "Rechercher par acteur", description = "Recherche les films d'un acteur par son nom")
    public ResponseEntity<Map<String, Object>> searchFilmsByActor(
            @RequestParam String nom,
            @RequestParam(required = false) Long utilisateurId) {

        Map<String, Object> results = filmService.searchFilmsByActor(nom, utilisateurId);
        return ResponseEntity.ok(results);
    }

    /**
     * Recherche de films par réalisateur
     */
    @GetMapping("/recherche/realisateur")
    @Operation(summary = "Rechercher par réalisateur", description = "Recherche les films d'un réalisateur par son nom")
    public ResponseEntity<Map<String, Object>> searchFilmsByDirector(
            @RequestParam String nom,
            @RequestParam(required = false) Long utilisateurId) {

        Map<String, Object> results = filmService.searchFilmsByDirector(nom, utilisateurId);
        return ResponseEntity.ok(results);
    }

    /**
     * Recherche universelle (films, acteurs, réalisateurs)
     */
    @GetMapping("/recherche/universelle")
    @Operation(summary = "Recherche universelle", description = "Recherche simultanée dans films, acteurs et réalisateurs")
    public ResponseEntity<com.cinema.dto.UniversalSearchResultDTO> universalSearch(
            @RequestParam String q,
            @RequestParam(required = false) Long utilisateurId,
            @RequestParam(required = false) Integer genre,
            @RequestParam(required = false) Integer annee) {

        com.cinema.dto.UniversalSearchResultDTO results = filmService.searchUniversal(q, utilisateurId, genre, annee);
        return ResponseEntity.ok(results);
    }
}
