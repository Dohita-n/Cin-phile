package com.cinema.repository;

import com.cinema.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour l'entité Genre
 */
@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    /**
     * Recherche un genre par libellé
     */
    Optional<Genre> findByLibelle(String libelle);
}
