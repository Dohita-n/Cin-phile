package com.cinema.service;

import com.cinema.model.Genre;
import com.cinema.model.Utilisateur;
import com.cinema.repository.GenreRepository;
import com.cinema.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service pour la gestion des utilisateurs
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final GenreRepository genreRepository;

    /**
     * Récupère un utilisateur par ID
     */
    public Utilisateur getUtilisateur(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
    }

    /**
     * Met à jour les préférences de genres d'un utilisateur
     */
    @Transactional
    public Utilisateur updatePreferences(Long utilisateurId, List<Long> genreIds) {
        log.info("Mise à jour préférences pour utilisateur: {}", utilisateurId);

        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        Set<Genre> preferences = new HashSet<>();
        for (Long genreId : genreIds) {
            Genre genre = genreRepository.findById(genreId)
                    .orElseThrow(() -> new IllegalArgumentException("Genre non trouvé: " + genreId));
            preferences.add(genre);
        }

        utilisateur.setPreferences(preferences);
        return utilisateurRepository.save(utilisateur);
    }

    /**
     * Récupère les préférences de genres d'un utilisateur
     */
    public Set<Genre> getPreferences(Long utilisateurId) {
        Utilisateur utilisateur = getUtilisateur(utilisateurId);
        return utilisateur.getPreferences();
    }
}
