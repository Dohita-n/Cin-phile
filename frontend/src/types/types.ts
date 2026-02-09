// Types TypeScript pour l'application

export interface User {
    id: number;
    nom: string;
    email: string;
}

export interface AuthResponse {
    token: string;
    type: string;
    id: number;
    nom: string;
    email: string;
}

export interface LoginRequest {
    email: string;
    motDePasse: string;
}

export interface RegisterRequest {
    nom: string;
    email: string;
    motDePasse: string;
}

export interface Genre {
    id: number;
    libelle: string;
}

export interface Acteur {
    id: number;
    nom: string;
    dateNaissance?: string;
    profilePath?: string;
}

export interface Realisateur {
    id: number;
    nom: string;
    dateNaissance?: string;
    profilePath?: string;
}

export interface Film {
    id: number;
    titre: string;
    anneeSortie?: number;
    synopsis?: string;
    paysOrigine?: string;
    note?: number;
    posterPath?: string;
    backdropPath?: string;
    realisateur?: Realisateur;
    acteurs?: Acteur[];
    genres?: Genre[];
}

export interface Favori {
    id: number;
    vu: boolean;
    dateAjout: string;
    commentaire?: string;
    film: Film;
}

export interface FavoriRequest {
    filmId: number;
    utilisateurId: number;
    commentaire?: string;
}

export interface UpdateVuRequest {
    vu?: boolean;
    commentaire?: string;
}

export interface TmdbMovie {
    id: number;
    title: string;
    original_title?: string;
    overview?: string;
    poster_path?: string;
    backdrop_path?: string;
    release_date?: string;
    vote_average?: number;
    genre_ids?: number[];
    genres?: Genre[];
}

export interface TmdbSearchResponse {
    page: number;
    results: TmdbMovie[];
    total_pages: number;
    total_results: number;
}

export interface SearchFilters {
    query?: string;
    genre?: number;
    annee?: number;
    page?: number;
}

export interface PersonWithFilms {
    id: number;
    name: string;
    profilePath?: string;
    films: TmdbMovie[];
}

export interface UniversalSearchResult {
    films?: TmdbSearchResponse;
    actors?: PersonWithFilms[];
    directors?: PersonWithFilms[];
}
