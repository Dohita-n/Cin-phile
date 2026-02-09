package com.cinema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entité Film
 * Représente un film avec ses relations (réalisateur, acteurs, genres)
 */
@Entity
@Table(name = "films")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "favoris")
@EqualsAndHashCode(exclude = "favoris")
public class Film {

    @Id
    private Long id; // ID TMDB

    @Column(nullable = false)
    private String titre;

    @Column(name = "annee_sortie")
    private Integer anneeSortie;

    @Column(columnDefinition = "TEXT")
    private String synopsis;

    @Column(name = "pays_origine")
    private String paysOrigine;

    private Double note;

    @Column(name = "poster_path")
    private String posterPath;

    @Column(name = "backdrop_path")
    private String backdropPath;

    // Relation ManyToOne avec Realisateur
    @ManyToOne
    @JoinColumn(name = "realisateur_id")
    private Realisateur realisateur;

    // Relation ManyToMany avec Acteur
    @ManyToMany
    @JoinTable(name = "film_acteur", joinColumns = @JoinColumn(name = "film_id"), inverseJoinColumns = @JoinColumn(name = "acteur_id"))
    private Set<Acteur> acteurs = new HashSet<>();

    // Relation ManyToMany avec Genre
    @ManyToMany
    @JoinTable(name = "film_genre", joinColumns = @JoinColumn(name = "film_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

    // Relation OneToMany avec Favori
    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Favori> favoris = new ArrayList<>();
}
