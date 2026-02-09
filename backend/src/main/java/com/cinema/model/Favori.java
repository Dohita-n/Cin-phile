package com.cinema.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entité Favori
 * Table d'association entre Utilisateur et Film avec attributs supplémentaires
 */
@Entity
@Table(name = "favoris")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean vu = false;

    @Column(name = "date_ajout", nullable = false)
    private LocalDateTime dateAjout;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    // Relation ManyToOne avec Utilisateur
    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    // Relation ManyToOne avec Film
    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    @PrePersist
    protected void onCreate() {
        dateAjout = LocalDateTime.now();
        if (vu == null) {
            vu = false;
        }
    }
}
