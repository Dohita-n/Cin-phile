package com.cinema.service;

import com.cinema.config.JwtUtil;
import com.cinema.dto.AuthResponse;
import com.cinema.dto.LoginRequest;
import com.cinema.dto.RegisterRequest;
import com.cinema.model.Utilisateur;
import com.cinema.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service pour l'authentification des utilisateurs
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    /**
     * Inscription d'un nouvel utilisateur
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Tentative d'inscription pour: {}", request.getEmail());

        // Vérifier si l'email existe déjà
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }

        // Créer le nouvel utilisateur
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(request.getNom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));

        utilisateur = utilisateurRepository.save(utilisateur);
        log.info("Utilisateur créé avec succès: {}", utilisateur.getEmail());

        // Générer le token JWT
        String token = jwtUtil.generateToken(utilisateur.getEmail(), utilisateur.getId());

        return new AuthResponse(token, utilisateur.getId(), utilisateur.getNom(), utilisateur.getEmail());
    }

    /**
     * Connexion d'un utilisateur
     */
    public AuthResponse login(LoginRequest request) {
        log.info("Tentative de connexion pour: {}", request.getEmail());

        // Authentifier l'utilisateur
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getMotDePasse()));

        // Récupérer l'utilisateur
        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email ou mot de passe incorrect"));

        log.info("Connexion réussie pour: {}", utilisateur.getEmail());

        // Générer le token JWT
        String token = jwtUtil.generateToken(utilisateur.getEmail(), utilisateur.getId());

        return new AuthResponse(token, utilisateur.getId(), utilisateur.getNom(), utilisateur.getEmail());
    }
}
