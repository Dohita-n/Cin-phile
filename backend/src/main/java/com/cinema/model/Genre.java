package com.cinema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

/**
 * Entité Genre
 * Représente un genre de film
 */
@Entity
@Table(name = "genres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "films", "utilisateurs" })
@EqualsAndHashCode(exclude = { "films", "utilisateurs" })
public class Genre {

    @Id
    private Long id; // ID TMDB

    @Column(nullable = false, unique = true)
    private String libelle;

    // Relation ManyToMany inverse avec Film
    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Film> films = new HashSet<>();

    // Relation ManyToMany inverse avec Utilisateur (préférences)
    @ManyToMany(mappedBy = "preferences", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Utilisateur> utilisateurs = new HashSet<>();
}
