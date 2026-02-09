package com.cinema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Entité Acteur
 * Représente un acteur de cinéma
 */
@Entity
@Table(name = "acteurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "films")
@EqualsAndHashCode(exclude = "films")
public class Acteur {

    @Id
    private Long id; // ID TMDB

    @Column(nullable = false)
    private String nom;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "profile_path")
    private String profilePath;

    // Relation ManyToMany inverse avec Film
    @ManyToMany(mappedBy = "acteurs", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Film> films = new HashSet<>();
}
