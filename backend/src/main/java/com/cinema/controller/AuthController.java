package com.cinema.controller;

import com.cinema.dto.AuthResponse;
import com.cinema.dto.LoginRequest;
import com.cinema.dto.RegisterRequest;
import com.cinema.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller pour l'authentification
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Endpoints pour l'inscription et la connexion")
public class AuthController {

    private final AuthService authService;

    /**
     * Inscription d'un nouvel utilisateur
     */
    @PostMapping("/register")
    @Operation(summary = "Inscription", description = "Créer un nouveau compte utilisateur")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Connexion d'un utilisateur
     */
    @PostMapping("/login")
    @Operation(summary = "Connexion", description = "Se connecter avec email et mot de passe")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
