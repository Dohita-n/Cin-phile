package com.cinema.repository;

import com.cinema.model.Realisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'entité Réalisateur
 */
@Repository
public interface RealisateurRepository extends JpaRepository<Realisateur, Long> {

    /**
     * Recherche des réalisateurs par nom
     */
    List<Realisateur> findByNomContainingIgnoreCase(String nom);
}
