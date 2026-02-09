package com.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la réponse d'authentification
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String nom;
    private String email;

    public AuthResponse(String token, Long id, String nom, String email) {
        this.token = token;
        this.id = id;
        this.nom = nom;
        this.email = email;
    }
}
