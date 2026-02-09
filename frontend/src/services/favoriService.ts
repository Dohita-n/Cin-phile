import api from './api';
import { Favori, FavoriRequest, UpdateVuRequest } from '../types/types';

export const favoriService = {
    /**
     * Ajoute un film aux favoris
     */
    ajouterFavori: async (data: FavoriRequest): Promise<Favori> => {
        const response = await api.post<Favori>('/favoris', data);
        return response.data;
    },

    /**
     * Récupère tous les favoris d'un utilisateur
     */
    getFavoris: async (utilisateurId: number, vu?: boolean): Promise<Favori[]> => {
        const params: any = { utilisateurId };
        if (vu !== undefined) {
            params.vu = vu;
        }
        const response = await api.get<Favori[]>('/favoris', { params });
        return response.data;
    },

    /**
     * Met à jour le statut "vu" d'un favori
     */
    updateVu: async (favoriId: number, data: UpdateVuRequest): Promise<Favori> => {
        const response = await api.put<Favori>(`/favoris/${favoriId}`, data);
        return response.data;
    },

    /**
     * Supprime un favori
     */
    supprimerFavori: async (favoriId: number): Promise<void> => {
        await api.delete(`/favoris/${favoriId}`);
    },

    /**
     * Vérifie si un film est dans les favoris
     */
    isFavori: async (utilisateurId: number, filmId: number): Promise<boolean> => {
        const response = await api.get<boolean>('/favoris/check', {
            params: { utilisateurId, filmId },
        });
        return response.data;
    },
};
