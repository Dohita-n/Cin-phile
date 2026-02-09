package com.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la mise à jour du statut "vu" d'un favori
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVuRequest {

    private Boolean vu;
    private String commentaire;
}
