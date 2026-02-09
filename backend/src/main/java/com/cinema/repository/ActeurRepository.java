package com.cinema.repository;

import com.cinema.model.Acteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'entité Acteur
 */
@Repository
public interface ActeurRepository extends JpaRepository<Acteur, Long> {

    /**
     * Recherche des acteurs par nom
     */
    List<Acteur> findByNomContainingIgnoreCase(String nom);
}
