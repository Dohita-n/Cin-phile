package com.cinema.controller;

import com.cinema.dto.FavoriRequest;
import com.cinema.dto.UpdateVuRequest;
import com.cinema.model.Favori;
import com.cinema.service.FavoriService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller pour la gestion des favoris
 */
@RestController
@RequestMapping("/api/favoris")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Favoris", description = "Endpoints pour la gestion des films favoris")
public class FavoriController {

    private final FavoriService favoriService;

    /**
     * Ajoute un film aux favoris
     */
    @PostMapping
    @Operation(summary = "Ajouter un favori", description = "Ajoute un film aux favoris de l'utilisateur")
    public ResponseEntity<Favori> ajouterFavori(@Valid @RequestBody FavoriRequest request) {
        Favori favori = favoriService.ajouterFavori(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(favori);
    }

    /**
     * Récupère tous les favoris d'un utilisateur
     */
    @GetMapping
    @Operation(summary = "Liste des favoris", description = "Récupère tous les favoris d'un utilisateur")
    public ResponseEntity<List<Favori>> getFavoris(
            @RequestParam Long utilisateurId,
            @RequestParam(required = false) Boolean vu) {

        List<Favori> favoris;
        if (vu != null) {
            favoris = favoriService.getFavorisByVu(utilisateurId, vu);
        } else {
            favoris = favoriService.getFavoris(utilisateurId);
        }
        return ResponseEntity.ok(favoris);
    }

    /**
     * Met à jour le statut "vu" d'un favori
     */
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un favori", description = "Met à jour le statut vu ou le commentaire")
    public ResponseEntity<Favori> updateVu(
            @PathVariable Long id,
            @RequestBody UpdateVuRequest request) {

        Favori favori = favoriService.updateVu(id, request);
        return ResponseEntity.ok(favori);
    }

    /**
     * Supprime un favori
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un favori", description = "Retire un film des favoris")
    public ResponseEntity<Void> supprimerFavori(@PathVariable Long id) {
        favoriService.supprimerFavori(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Vérifie si un film est favori
     */
    @GetMapping("/check")
    @Operation(summary = "Vérifier si favori", description = "Vérifie si un film est dans les favoris")
    public ResponseEntity<Boolean> isFavori(
            @RequestParam Long utilisateurId,
            @RequestParam Long filmId) {

        boolean isFavori = favoriService.isFavori(utilisateurId, filmId);
        return ResponseEntity.ok(isFavori);
    }
}
