package com.cinema.service;

import com.cinema.model.*;
import com.cinema.repository.FavoriRepository;
import com.cinema.repository.HistoriqueRechercheRepository;
import com.cinema.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service pour générer des recommandations personnalisées
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecommandationService {

    private final UtilisateurRepository utilisateurRepository;
    private final FavoriRepository favoriRepository;
    private final HistoriqueRechercheRepository historiqueRechercheRepository;
    private final TmdbService tmdbService;

    /**
     * Génère des recommandations personnalisées pour un utilisateur
     */
    public List<Map<String, Object>> getRecommandations(Long utilisateurId) {
        log.info("Génération recommandations pour utilisateur: {}", utilisateurId);

        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        List<Map<String, Object>> recommandations = new ArrayList<>();
        Set<Long> filmIdsVus = new HashSet<>();

        // 1. Recommandations basées sur les favoris
        List<Favori> favoris = favoriRepository.findByUtilisateurId(utilisateurId);
        for (Favori favori : favoris) {
            filmIdsVus.add(favori.getFilm().getId());

            // Récupérer des films similaires
            try {
                Map<String, Object> similaires = tmdbService.getSimilarMovies(favori.getFilm().getId(), 1);
                List<Map<String, Object>> results = (List<Map<String, Object>>) similaires.get("results");
                if (results != null) {
                    recommandations.addAll(results.stream()
                            .limit(3)
                            .collect(Collectors.toList()));
                }
            } catch (Exception e) {
                log.error("Erreur récupération films similaires: {}", e.getMessage());
            }
        }

        // 2. Recommandations basées sur les préférences de genres
        Set<Genre> preferences = utilisateur.getPreferences();
        if (!preferences.isEmpty()) {
            for (Genre genre : preferences) {
                try {
                    Map<String, Object> filmsByGenre = tmdbService.discoverMoviesByGenre(genre.getId(), 1);
                    List<Map<String, Object>> results = (List<Map<String, Object>>) filmsByGenre.get("results");
                    if (results != null) {
                        recommandations.addAll(results.stream()
                                .limit(5)
                                .collect(Collectors.toList()));
                    }
                } catch (Exception e) {
                    log.error("Erreur récupération films par genre: {}", e.getMessage());
                }
            }
        }

        // 3. Recommandations basées sur l'historique de recherche
        List<HistoriqueRecherche> historique = historiqueRechercheRepository
                .findByUtilisateurIdOrderByDateRechercheDesc(utilisateurId);

        // Analyser les genres les plus recherchés
        Map<String, Long> genreFrequency = historique.stream()
                .filter(h -> h.getGenreRecherche() != null)
                .collect(Collectors.groupingBy(
                        HistoriqueRecherche::getGenreRecherche,
                        Collectors.counting()));

        // Récupérer des films des genres les plus recherchés
        genreFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(2)
                .forEach(entry -> {
                    try {
                        Long genreId = Long.parseLong(entry.getKey());
                        Map<String, Object> filmsByGenre = tmdbService.discoverMoviesByGenre(genreId, 1);
                        List<Map<String, Object>> results = (List<Map<String, Object>>) filmsByGenre.get("results");
                        if (results != null) {
                            recommandations.addAll(results.stream()
                                    .limit(3)
                                    .collect(Collectors.toList()));
                        }
                    } catch (Exception e) {
                        log.error("Erreur récupération films par historique: {}", e.getMessage());
                    }
                });

        // 4. Si pas assez de recommandations, ajouter des films populaires
        if (recommandations.size() < 10) {
            try {
                Map<String, Object> populaires = tmdbService.getPopularMovies(1);
                List<Map<String, Object>> results = (List<Map<String, Object>>) populaires.get("results");
                if (results != null) {
                    recommandations.addAll(results.stream()
                            .limit(10 - recommandations.size())
                            .collect(Collectors.toList()));
                }
            } catch (Exception e) {
                log.error("Erreur récupération films populaires: {}", e.getMessage());
            }
        }

        // Filtrer les doublons et les films déjà vus, limiter à 20
        return recommandations.stream()
                .filter(film -> {
                    Long filmId = ((Number) film.get("id")).longValue();
                    return !filmIdsVus.contains(filmId);
                })
                .collect(Collectors.toMap(
                        film -> ((Number) film.get("id")).longValue(),
                        film -> film,
                        (existing, replacement) -> existing))
                .values()
                .stream()
                .limit(20)
                .collect(Collectors.toList());
    }
}
