package com.cinema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité Réalisateur
 * Représente un réalisateur de cinéma
 */
@Entity
@Table(name = "realisateurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "films")
@EqualsAndHashCode(exclude = "films")
public class Realisateur {

    @Id
    private Long id; // ID TMDB

    @Column(nullable = false)
    private String nom;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "profile_path")
    private String profilePath;

    // Relation OneToMany inverse avec Film
    @OneToMany(mappedBy = "realisateur", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Film> films = new ArrayList<>();
}
