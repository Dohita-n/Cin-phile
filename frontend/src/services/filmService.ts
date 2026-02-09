import api from './api';
import { Genre, SearchFilters, TmdbSearchResponse, UniversalSearchResult } from '../types/types';

export const filmService = {
    /**
     * Recherche de films avec filtres
     */
    searchFilms: async (filters: SearchFilters, utilisateurId?: number): Promise<TmdbSearchResponse> => {
        const params: any = { ...filters };
        if (utilisateurId) {
            params.utilisateurId = utilisateurId;
        }
        const response = await api.get<TmdbSearchResponse>('/films', { params });
        return response.data;
    },

    /**
     * Récupère les détails d'un film
     */
    getFilmDetails: async (filmId: number, utilisateurId?: number): Promise<any> => {
        const params: any = {};
        if (utilisateurId) {
            params.utilisateurId = utilisateurId;
        }
        const response = await api.get(`/films/${filmId}`, { params });
        return response.data;
    },

    /**
     * Récupère les films populaires
     */
    getPopularFilms: async (page: number = 1): Promise<TmdbSearchResponse> => {
        const response = await api.get<TmdbSearchResponse>('/films/populaires', {
            params: { page },
        });
        return response.data;
    },

    /**
     * Récupère la liste des genres
     */
    getGenres: async (): Promise<Genre[]> => {
        const response = await api.get<Genre[]>('/films/genres');
        return response.data;
    },

    /**
     * Recherche universelle (films, acteurs, réalisateurs)
     */
    universalSearch: async (query: string, utilisateurId?: number, genre?: number, annee?: number): Promise<UniversalSearchResult> => {
        const params: any = { q: query };
        if (utilisateurId) {
            params.utilisateurId = utilisateurId;
        }
        if (genre) {
            params.genre = genre;
        }
        if (annee) {
            params.annee = annee;
        }
        const response = await api.get<UniversalSearchResult>('/films/recherche/universelle', { params });
        return response.data;
    },
};
