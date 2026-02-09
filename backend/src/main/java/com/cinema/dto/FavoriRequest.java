package com.cinema.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la requête d'ajout de favori
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriRequest {

    @NotNull(message = "L'ID du film est obligatoire")
    private Long filmId;

    @NotNull(message = "L'ID de l'utilisateur est obligatoire")
    private Long utilisateurId;

    private String commentaire;
}
