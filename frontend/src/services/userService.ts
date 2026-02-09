import api from './api';
import { Genre } from '../types/types';

export const userService = {
    /**
     * Met à jour les préférences de genres
     */
    updatePreferences: async (utilisateurId: number, genreIds: number[]): Promise<any> => {
        const response = await api.put(`/utilisateurs/${utilisateurId}/preferences`, genreIds);
        return response.data;
    },

    /**
     * Récupère les préférences de genres
     */
    getPreferences: async (utilisateurId: number): Promise<Genre[]> => {
        const response = await api.get<Genre[]>(`/utilisateurs/${utilisateurId}/preferences`);
        return response.data;
    },

    /**
     * Récupère les recommandations personnalisées
     */
    getRecommandations: async (utilisateurId: number): Promise<any[]> => {
        const response = await api.get<any[]>('/recommandations', {
            params: { utilisateurId },
        });
        return response.data;
    },
};
