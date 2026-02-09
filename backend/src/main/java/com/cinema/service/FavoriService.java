package com.cinema.service;

import com.cinema.dto.FavoriRequest;
import com.cinema.dto.UpdateVuRequest;
import com.cinema.model.Favori;
import com.cinema.model.Film;
import com.cinema.model.Utilisateur;
import com.cinema.repository.FavoriRepository;
import com.cinema.repository.FilmRepository;
import com.cinema.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Service pour la gestion des favoris
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriService {

    private final FavoriRepository favoriRepository;
    private final FilmRepository filmRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final FilmService filmService;
    private final TmdbService tmdbService;

    /**
     * Ajoute un film aux favoris
     */
    @Transactional
    public Favori ajouterFavori(FavoriRequest request) {
        log.info("Ajout favori: film={}, utilisateur={}", request.getFilmId(), request.getUtilisateurId());

        // Vérifier si le favori existe déjà
        if (favoriRepository.existsByUtilisateurIdAndFilmId(request.getUtilisateurId(), request.getFilmId())) {
            throw new IllegalArgumentException("Ce film est déjà dans vos favoris");
        }

        // Récupérer l'utilisateur
        Utilisateur utilisateur = utilisateurRepository.findById(request.getUtilisateurId())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        // Récupérer ou créer le film
        Film film = filmRepository.findById(request.getFilmId()).orElse(null);
        if (film == null) {
            // Récupérer les détails depuis TMDB et sauvegarder
            Map<String, Object> filmDetails = tmdbService.getMovieDetails(request.getFilmId());
            film = filmService.saveFilmFromTmdb(filmDetails);
        }

        // Créer le favori
        Favori favori = new Favori();
        favori.setUtilisateur(utilisateur);
        favori.setFilm(film);
        favori.setCommentaire(request.getCommentaire());

        return favoriRepository.save(favori);
    }

    /**
     * Récupère tous les favoris d'un utilisateur
     */
    public List<Favori> getFavoris(Long utilisateurId) {
        log.info("Récupération favoris pour utilisateur: {}", utilisateurId);
        return favoriRepository.findByUtilisateurIdOrderByDateAjoutDesc(utilisateurId);
    }

    /**
     * Récupère les favoris filtrés par statut "vu"
     */
    public List<Favori> getFavorisByVu(Long utilisateurId, Boolean vu) {
        log.info("Récupération favoris (vu={}) pour utilisateur: {}", vu, utilisateurId);
        return favoriRepository.findByUtilisateurIdAndVu(utilisateurId, vu);
    }

    /**
     * Met à jour le statut "vu" d'un favori
     */
    @Transactional
    public Favori updateVu(Long favoriId, UpdateVuRequest request) {
        log.info("Mise à jour statut vu pour favori: {}", favoriId);

        Favori favori = favoriRepository.findById(favoriId)
                .orElseThrow(() -> new IllegalArgumentException("Favori non trouvé"));

        if (request.getVu() != null) {
            favori.setVu(request.getVu());
        }
        if (request.getCommentaire() != null) {
            favori.setCommentaire(request.getCommentaire());
        }

        return favoriRepository.save(favori);
    }

    /**
     * Supprime un favori
     */
    @Transactional
    public void supprimerFavori(Long favoriId) {
        log.info("Suppression favori: {}", favoriId);

        if (!favoriRepository.existsById(favoriId)) {
            throw new IllegalArgumentException("Favori non trouvé");
        }

        favoriRepository.deleteById(favoriId);
    }

    /**
     * Vérifie si un film est dans les favoris d'un utilisateur
     */
    public boolean isFavori(Long utilisateurId, Long filmId) {
        return favoriRepository.existsByUtilisateurIdAndFilmId(utilisateurId, filmId);
    }
}
