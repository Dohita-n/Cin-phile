package com.cinema.service;

import com.cinema.model.*;
import com.cinema.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des films
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmRepository filmRepository;
    private final ActeurRepository acteurRepository;
    private final RealisateurRepository realisateurRepository;
    private final GenreRepository genreRepository;
    private final HistoriqueRechercheRepository historiqueRechercheRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final TmdbService tmdbService;

    /**
     * Recherche de films via TMDB avec sauvegarde de l'historique
     */
    @Transactional
    public Map<String, Object> searchFilms(String query, Long genreId, Integer annee, Long utilisateurId,
            Integer page) {
        log.info("Recherche de films: query={}, genre={}, annee={}", query, genreId, annee);

        Map<String, Object> results;

        if (query != null && !query.isEmpty()) {
            results = tmdbService.searchMovies(query, page);

            // Enregistrer dans l'historique
            if (utilisateurId != null) {
                saveSearchHistory(utilisateurId, query, null);
            }
        } else {
            results = tmdbService.discoverMovies(annee, genreId, null, page);

            // Enregistrer dans l'historique
            if (utilisateurId != null && genreId != null) {
                saveSearchHistory(utilisateurId, null, genreId.toString());
            }
        }

        return results;
    }

    /**
     * Récupère les détails d'un film et le sauvegarde en base
     */
    @Transactional
    public Map<String, Object> getFilmDetails(Long filmId, Long utilisateurId) {
        log.info("Récupération détails film: {}", filmId);

        Map<String, Object> details = tmdbService.getMovieDetails(filmId);

        // Sauvegarder le film en base
        saveFilmFromTmdb(details);

        // Enregistrer dans l'historique
        if (utilisateurId != null) {
            saveSearchHistory(utilisateurId, null, null, filmId);
        }

        return details;
    }

    /**
     * Sauvegarde un film depuis les données TMDB
     */
    @Transactional
    public Film saveFilmFromTmdb(Map<String, Object> tmdbData) {
        Long filmId = ((Number) tmdbData.get("id")).longValue();

        Film film = filmRepository.findById(filmId).orElse(new Film());
        film.setId(filmId);
        film.setTitre((String) tmdbData.get("title"));

        String releaseDateStr = (String) tmdbData.get("release_date");
        if (releaseDateStr != null && !releaseDateStr.isEmpty()) {
            film.setAnneeSortie(Integer.parseInt(releaseDateStr.substring(0, 4)));
        }

        film.setSynopsis((String) tmdbData.get("overview"));
        film.setPosterPath((String) tmdbData.get("poster_path"));
        film.setBackdropPath((String) tmdbData.get("backdrop_path"));

        Object voteAverage = tmdbData.get("vote_average");
        if (voteAverage != null) {
            film.setNote(((Number) voteAverage).doubleValue());
        }

        // Gérer les genres
        List<Map<String, Object>> genresData = (List<Map<String, Object>>) tmdbData.get("genres");
        if (genresData != null) {
            Set<Genre> genres = new HashSet<>();
            for (Map<String, Object> genreData : genresData) {
                Long genreId = ((Number) genreData.get("id")).longValue();
                Genre genre = genreRepository.findById(genreId).orElse(new Genre());
                genre.setId(genreId);
                genre.setLibelle((String) genreData.get("name"));
                genre = genreRepository.save(genre);
                genres.add(genre);
            }
            film.setGenres(genres);
        }

        // Gérer le réalisateur (depuis credits)
        Map<String, Object> credits = (Map<String, Object>) tmdbData.get("credits");
        if (credits != null) {
            List<Map<String, Object>> crew = (List<Map<String, Object>>) credits.get("crew");
            if (crew != null) {
                Optional<Map<String, Object>> directorData = crew.stream()
                        .filter(c -> "Director".equals(c.get("job")))
                        .findFirst();

                if (directorData.isPresent()) {
                    Long directorId = ((Number) directorData.get().get("id")).longValue();
                    Realisateur realisateur = realisateurRepository.findById(directorId).orElse(new Realisateur());
                    realisateur.setId(directorId);
                    realisateur.setNom((String) directorData.get().get("name"));
                    realisateur.setProfilePath((String) directorData.get().get("profile_path"));
                    realisateur = realisateurRepository.save(realisateur);
                    film.setRealisateur(realisateur);
                }
            }

            // Gérer les acteurs (top 10)
            List<Map<String, Object>> cast = (List<Map<String, Object>>) credits.get("cast");
            if (cast != null) {
                Set<Acteur> acteurs = new HashSet<>();
                int count = 0;
                for (Map<String, Object> actorData : cast) {
                    if (count >= 10)
                        break;

                    Long actorId = ((Number) actorData.get("id")).longValue();
                    Acteur acteur = acteurRepository.findById(actorId).orElse(new Acteur());
                    acteur.setId(actorId);
                    acteur.setNom((String) actorData.get("name"));
                    acteur.setProfilePath((String) actorData.get("profile_path"));
                    acteur = acteurRepository.save(acteur);
                    acteurs.add(acteur);
                    count++;
                }
                film.setActeurs(acteurs);
            }
        }

        return filmRepository.save(film);
    }

    /**
     * Enregistre une recherche dans l'historique
     */
    private void saveSearchHistory(Long utilisateurId, String query, String genreId) {
        saveSearchHistory(utilisateurId, query, genreId, null);
    }

    private void saveSearchHistory(Long utilisateurId, String query, String genreId, Long filmId) {
        try {
            Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId).orElse(null);
            if (utilisateur != null) {
                HistoriqueRecherche historique = new HistoriqueRecherche();
                historique.setUtilisateur(utilisateur);
                historique.setRequeteRecherche(query);
                historique.setGenreRecherche(genreId);
                historique.setFilmId(filmId);
                historiqueRechercheRepository.save(historique);
            }
        } catch (Exception e) {
            log.error("Erreur lors de la sauvegarde de l'historique: {}", e.getMessage());
        }
    }

    /**
     * Récupère les films populaires
     */
    public Map<String, Object> getPopularFilms(Integer page) {
        return tmdbService.getPopularMovies(page);
    }

    /**
     * Récupère les genres depuis TMDB et les sauvegarde
     */
    @Transactional
    public List<Genre> getGenres() {
        Map<String, Object> response = tmdbService.getGenres();
        List<Map<String, Object>> genresData = (List<Map<String, Object>>) response.get("genres");

        List<Genre> genres = new ArrayList<>();
        for (Map<String, Object> genreData : genresData) {
            Long genreId = ((Number) genreData.get("id")).longValue();
            Genre genre = genreRepository.findById(genreId).orElse(new Genre());
            genre.setId(genreId);
            genre.setLibelle((String) genreData.get("name"));
            genres.add(genreRepository.save(genre));
        }

        return genres;
    }

    /**
     * Recherche de films par acteur
     */
    @Transactional
    public Map<String, Object> searchFilmsByActor(String actorName, Long utilisateurId) {
        log.info("Recherche de films par acteur: {}", actorName);

        // Rechercher l'acteur dans TMDB
        Map<String, Object> personResults = tmdbService.searchPerson(actorName, 1);
        List<Map<String, Object>> people = (List<Map<String, Object>>) personResults.get("results");

        if (people == null || people.isEmpty()) {
            // Aucun acteur trouvé
            Map<String, Object> emptyResult = new HashMap<>();
            emptyResult.put("results", new ArrayList<>());
            emptyResult.put("total_results", 0);
            return emptyResult;
        }

        // Prendre le premier résultat (le plus pertinent)
        Map<String, Object> actor = people.get(0);
        Long actorId = ((Number) actor.get("id")).longValue();

        // Récupérer les films de cet acteur
        Map<String, Object> credits = tmdbService.getPersonMovieCredits(actorId);
        List<Map<String, Object>> cast = (List<Map<String, Object>>) credits.get("cast");

        // Trier par popularité (vote_count)
        if (cast != null) {
            cast.sort((a, b) -> {
                Number voteA = (Number) a.getOrDefault("vote_count", 0);
                Number voteB = (Number) b.getOrDefault("vote_count", 0);
                return Double.compare(voteB.doubleValue(), voteA.doubleValue());
            });
        }

        // Enregistrer dans l'historique
        if (utilisateurId != null) {
            saveSearchHistory(utilisateurId, "Acteur: " + actorName, null);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("results", cast != null ? cast : new ArrayList<>());
        result.put("total_results", cast != null ? cast.size() : 0);
        result.put("person", actor);
        return result;
    }

    /**
     * Recherche de films par réalisateur
     */
    @Transactional
    public Map<String, Object> searchFilmsByDirector(String directorName, Long utilisateurId) {
        log.info("Recherche de films par réalisateur: {}", directorName);

        // Rechercher le réalisateur dans TMDB
        Map<String, Object> personResults = tmdbService.searchPerson(directorName, 1);
        List<Map<String, Object>> people = (List<Map<String, Object>>) personResults.get("results");

        if (people == null || people.isEmpty()) {
            // Aucun réalisateur trouvé
            Map<String, Object> emptyResult = new HashMap<>();
            emptyResult.put("results", new ArrayList<>());
            emptyResult.put("total_results", 0);
            return emptyResult;
        }

        // Prendre le premier résultat (le plus pertinent)
        Map<String, Object> director = people.get(0);
        Long directorId = ((Number) director.get("id")).longValue();

        // Récupérer les films de ce réalisateur
        Map<String, Object> credits = tmdbService.getPersonMovieCredits(directorId);
        List<Map<String, Object>> crew = (List<Map<String, Object>>) credits.get("crew");

        // Filtrer pour ne garder que les films où la personne est réalisateur
        List<Map<String, Object>> directedFilms = new ArrayList<>();
        if (crew != null) {
            for (Map<String, Object> credit : crew) {
                if ("Director".equals(credit.get("job"))) {
                    directedFilms.add(credit);
                }
            }

            // Trier par popularité (vote_count)
            directedFilms.sort((a, b) -> {
                Number voteA = (Number) a.getOrDefault("vote_count", 0);
                Number voteB = (Number) b.getOrDefault("vote_count", 0);
                return Double.compare(voteB.doubleValue(), voteA.doubleValue());
            });
        }

        // Enregistrer dans l'historique
        if (utilisateurId != null) {
            saveSearchHistory(utilisateurId, "Réalisateur: " + directorName, null);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("results", directedFilms);
        result.put("total_results", directedFilms.size());
        result.put("person", director);
        return result;
    }

    /**
     * Recherche universelle (films, acteurs, réalisateurs)
     */
    @Transactional
    public com.cinema.dto.UniversalSearchResultDTO searchUniversal(String query, Long utilisateurId, Integer genreId,
            Integer year) {
        log.info("Recherche universelle: {} [Genre: {}, Annee: {}]", query, genreId, year);

        com.cinema.dto.UniversalSearchResultDTO result = new com.cinema.dto.UniversalSearchResultDTO();
        result.setActors(new ArrayList<>());
        result.setDirectors(new ArrayList<>());

        if (query == null || query.trim().length() < 3) {
            return result;
        }

        // 1. Rechercher les films (via TMDB) avec filtre année
        Map<String, Object> filmResults = tmdbService.searchMovies(query, 1, year);
        if (filmResults != null) {
            List<Map<String, Object>> films = (List<Map<String, Object>>) filmResults.get("results");

            if (films != null) {
                // Filtrage par genre si nécessaire
                if (genreId != null) {
                    films = films.stream()
                            .filter(f -> {
                                List<Integer> ids = (List<Integer>) f.get("genre_ids");
                                return ids != null && ids.contains(genreId);
                            })
                            .collect(Collectors.toList());
                }

                // Limiter à 10 résultats après filtrage
                if (films.size() > 10) {
                    films = films.subList(0, 10);
                }
                // Mettre à jour la liste filtrée dans le résultat
                filmResults.put("results", films);
            }
            result.setFilms(filmResults);
        }

        // 2. Rechercher les personnes (acteurs et réalisateurs)
        Map<String, Object> personResults = tmdbService.searchPerson(query, 1);
        List<Map<String, Object>> people = (List<Map<String, Object>>) personResults.get("results");

        if (people != null) {
            // Pour chaque personne trouvée (limité aux 5 premiers pour la perf)
            int count = 0;
            for (Map<String, Object> person : people) {
                if (count >= 5)
                    break;

                Long personId = ((Number) person.get("id")).longValue();
                String name = (String) person.get("name");
                String profilePath = (String) person.get("profile_path");
                String knownFor = (String) person.get("known_for_department");

                // Récupérer la filmographie
                Map<String, Object> credits = tmdbService.getPersonMovieCredits(personId);

                com.cinema.dto.PersonWithFilmsDTO personDTO = new com.cinema.dto.PersonWithFilmsDTO();
                personDTO.setId(personId);
                personDTO.setName(name);
                personDTO.setProfilePath(profilePath);

                if ("Acting".equals(knownFor)) {
                    // C'est un acteur
                    List<Map<String, Object>> cast = (List<Map<String, Object>>) credits.get("cast");
                    if (cast != null) {
                        // Trier par popularité
                        cast.sort((a, b) -> {
                            Number voteA = (Number) a.getOrDefault("vote_count", 0);
                            Number voteB = (Number) b.getOrDefault("vote_count", 0);
                            return Double.compare(voteB.doubleValue(), voteA.doubleValue());
                        });
                        // Limiter à 10 films
                        if (cast.size() > 10)
                            cast = cast.subList(0, 10);
                        personDTO.setFilms(cast);
                        result.getActors().add(personDTO);
                    }
                } else if ("Directing".equals(knownFor)) {
                    // C'est un réalisateur
                    List<Map<String, Object>> crew = (List<Map<String, Object>>) credits.get("crew");
                    if (crew != null) {
                        List<Map<String, Object>> directed = crew.stream()
                                .filter(c -> "Director".equals(c.get("job")))
                                .sorted((a, b) -> {
                                    Number voteA = (Number) a.getOrDefault("vote_count", 0);
                                    Number voteB = (Number) b.getOrDefault("vote_count", 0);
                                    return Double.compare(voteB.doubleValue(), voteA.doubleValue());
                                })
                                .limit(10)
                                .collect(Collectors.toList());

                        if (!directed.isEmpty()) {
                            personDTO.setFilms(directed);
                            result.getDirectors().add(personDTO);
                        }
                    }
                }
                count++;
            }
        }

        // Enregistrer dans l'historique
        if (utilisateurId != null) {
            saveSearchHistory(utilisateurId, query, null);
        }

        return result;
    }
}
