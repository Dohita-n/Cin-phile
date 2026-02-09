package com.cinema.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entité HistoriqueRecherche
 * Stocke l'historique des recherches pour le système de recommandations
 */
@Entity
@Table(name = "historique_recherche")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueRecherche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @Column(name = "requete_recherche")
    private String requeteRecherche;

    @Column(name = "genre_recherche")
    private String genreRecherche;

    @Column(name = "film_id")
    private Long filmId;

    @Column(name = "date_recherche", nullable = false)
    private LocalDateTime dateRecherche;

    @PrePersist
    protected void onCreate() {
        dateRecherche = LocalDateTime.now();
    }
}
