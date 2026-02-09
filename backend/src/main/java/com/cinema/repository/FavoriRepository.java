package com.cinema.repository;

import com.cinema.model.Favori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Favori
 */
@Repository
public interface FavoriRepository extends JpaRepository<Favori, Long> {

    /**
     * Récupère tous les favoris d'un utilisateur
     */
    List<Favori> findByUtilisateurId(Long utilisateurId);

    /**
     * Récupère les favoris vus d'un utilisateur
     */
    List<Favori> findByUtilisateurIdAndVu(Long utilisateurId, Boolean vu);

    /**
     * Vérifie si un film est déjà dans les favoris d'un utilisateur
     */
    boolean existsByUtilisateurIdAndFilmId(Long utilisateurId, Long filmId);

    /**
     * Récupère un favori spécifique par utilisateur et film
     */
    Optional<Favori> findByUtilisateurIdAndFilmId(Long utilisateurId, Long filmId);

    /**
     * Récupère les films favoris triés par date d'ajout
     */
    @Query("SELECT f FROM Favori f WHERE f.utilisateur.id = :utilisateurId ORDER BY f.dateAjout DESC")
    List<Favori> findByUtilisateurIdOrderByDateAjoutDesc(@Param("utilisateurId") Long utilisateurId);
}
