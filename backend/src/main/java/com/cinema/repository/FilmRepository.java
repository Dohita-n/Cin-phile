package com.cinema.repository;

import com.cinema.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'entité Film
 */
@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {

    /**
     * Recherche des films par titre (contient)
     */
    List<Film> findByTitreContainingIgnoreCase(String titre);

    /**
     * Recherche des films par année de sortie
     */
    List<Film> findByAnneeSortie(Integer annee);

    /**
     * Recherche des films par genre
     */
    @Query("SELECT DISTINCT f FROM Film f JOIN f.genres g WHERE g.id = :genreId")
    List<Film> findByGenreId(@Param("genreId") Long genreId);

    /**
     * Recherche des films par réalisateur
     */
    @Query("SELECT f FROM Film f WHERE f.realisateur.id = :realisateurId")
    List<Film> findByRealisateurId(@Param("realisateurId") Long realisateurId);

    /**
     * Recherche des films par acteur
     */
    @Query("SELECT DISTINCT f FROM Film f JOIN f.acteurs a WHERE a.id = :acteurId")
    List<Film> findByActeurId(@Param("acteurId") Long acteurId);
}
