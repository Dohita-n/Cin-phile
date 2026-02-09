package com.cinema.repository;

import com.cinema.model.HistoriqueRecherche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'entité HistoriqueRecherche
 */
@Repository
public interface HistoriqueRechercheRepository extends JpaRepository<HistoriqueRecherche, Long> {

    /**
     * Récupère l'historique de recherche d'un utilisateur
     */
    List<HistoriqueRecherche> findByUtilisateurIdOrderByDateRechercheDesc(Long utilisateurId);

    /**
     * Récupère les dernières recherches d'un utilisateur (limitées)
     */
    @Query("SELECT h FROM HistoriqueRecherche h WHERE h.utilisateur.id = :utilisateurId ORDER BY h.dateRecherche DESC")
    List<HistoriqueRecherche> findRecentSearches(@Param("utilisateurId") Long utilisateurId);
}
