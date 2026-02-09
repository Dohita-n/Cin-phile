package com.cinema.controller;

import com.cinema.service.RecommandationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller pour les recommandations
 */
@RestController
@RequestMapping("/api/recommandations")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Recommandations", description = "Endpoints pour les recommandations personnalisées")
public class RecommandationController {

    private final RecommandationService recommandationService;

    /**
     * Récupère les recommandations personnalisées
     */
    @GetMapping
    @Operation(summary = "Recommandations personnalisées", description = "Génère des recommandations basées sur les favoris, préférences et historique")
    public ResponseEntity<List<Map<String, Object>>> getRecommandations(
            @RequestParam Long utilisateurId) {

        List<Map<String, Object>> recommandations = recommandationService.getRecommandations(utilisateurId);
        return ResponseEntity.ok(recommandations);
    }
}
