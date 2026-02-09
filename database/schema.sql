-- Script de création de la base de données Cinema
-- MySQL

CREATE DATABASE IF NOT EXISTS cinema_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cinema_db;

-- Table Utilisateurs
CREATE TABLE IF NOT EXISTS utilisateurs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table Réalisateurs
CREATE TABLE IF NOT EXISTS realisateurs (
    id BIGINT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    date_naissance DATE,
    profile_path VARCHAR(255),
    INDEX idx_nom (nom)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table Acteurs
CREATE TABLE IF NOT EXISTS acteurs (
    id BIGINT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    date_naissance DATE,
    profile_path VARCHAR(255),
    INDEX idx_nom (nom)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table Genres
CREATE TABLE IF NOT EXISTS genres (
    id BIGINT PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE,
    INDEX idx_libelle (libelle)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table Films
CREATE TABLE IF NOT EXISTS films (
    id BIGINT PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    annee_sortie INT,
    synopsis TEXT,
    pays_origine VARCHAR(100),
    note DOUBLE,
    poster_path VARCHAR(255),
    backdrop_path VARCHAR(255),
    realisateur_id BIGINT,
    FOREIGN KEY (realisateur_id) REFERENCES realisateurs(id) ON DELETE SET NULL,
    INDEX idx_titre (titre),
    INDEX idx_annee (annee_sortie),
    INDEX idx_note (note)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table d'association Film-Acteur (ManyToMany)
CREATE TABLE IF NOT EXISTS film_acteur (
    film_id BIGINT NOT NULL,
    acteur_id BIGINT NOT NULL,
    PRIMARY KEY (film_id, acteur_id),
    FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
    FOREIGN KEY (acteur_id) REFERENCES acteurs(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table d'association Film-Genre (ManyToMany)
CREATE TABLE IF NOT EXISTS film_genre (
    film_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table Favoris
CREATE TABLE IF NOT EXISTS favoris (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vu BOOLEAN NOT NULL DEFAULT FALSE,
    date_ajout DATETIME NOT NULL,
    commentaire TEXT,
    utilisateur_id BIGINT NOT NULL,
    film_id BIGINT NOT NULL,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
    UNIQUE KEY unique_favori (utilisateur_id, film_id),
    INDEX idx_utilisateur (utilisateur_id),
    INDEX idx_film (film_id),
    INDEX idx_vu (vu),
    INDEX idx_date_ajout (date_ajout)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table des préférences utilisateur-genre (ManyToMany)
CREATE TABLE IF NOT EXISTS utilisateur_preferences (
    utilisateur_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    PRIMARY KEY (utilisateur_id, genre_id),
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table Historique de recherche
CREATE TABLE IF NOT EXISTS historique_recherche (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    utilisateur_id BIGINT NOT NULL,
    requete_recherche VARCHAR(255),
    genre_recherche VARCHAR(50),
    film_id BIGINT,
    date_recherche DATETIME NOT NULL,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    INDEX idx_utilisateur (utilisateur_id),
    INDEX idx_date (date_recherche)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
