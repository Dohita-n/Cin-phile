package com.cinema.controller;

import com.cinema.model.Genre;
import com.cinema.model.Utilisateur;
import com.cinema.service.UtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Controller pour la gestion des utilisateurs
 */
@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Utilisateurs", description = "Endpoints pour la gestion des utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    /**
     * Récupère un utilisateur
     */
    @GetMapping("/{id}")
    @Operation(summary = "Détails utilisateur", description = "Récupère les informations d'un utilisateur")
    public ResponseEntity<Utilisateur> getUtilisateur(@PathVariable Long id) {
        Utilisateur utilisateur = utilisateurService.getUtilisateur(id);
        return ResponseEntity.ok(utilisateur);
    }

    /**
     * Met à jour les préférences de genres
     */
    @PutMapping("/{id}/preferences")
    @Operation(summary = "Mettre à jour les préférences", description = "Définit les genres préférés de l'utilisateur")
    public ResponseEntity<Utilisateur> updatePreferences(
            @PathVariable Long id,
            @RequestBody List<Long> genreIds) {

        Utilisateur utilisateur = utilisateurService.updatePreferences(id, genreIds);
        return ResponseEntity.ok(utilisateur);
    }

    /**
     * Récupère les préférences de genres
     */
    @GetMapping("/{id}/preferences")
    @Operation(summary = "Récupérer les préférences", description = "Récupère les genres préférés de l'utilisateur")
    public ResponseEntity<Set<Genre>> getPreferences(@PathVariable Long id) {
        Set<Genre> preferences = utilisateurService.getPreferences(id);
        return ResponseEntity.ok(preferences);
    }
}
