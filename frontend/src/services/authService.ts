import api from './api';
import { AuthResponse, LoginRequest, RegisterRequest } from '../types/types';

export const authService = {
    /**
     * Inscription d'un nouvel utilisateur
     */
    register: async (data: RegisterRequest): Promise<AuthResponse> => {
        const response = await api.post<AuthResponse>('/auth/register', data);
        if (response.data.token) {
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('user', JSON.stringify({
                id: response.data.id,
                nom: response.data.nom,
                email: response.data.email,
            }));
        }
        return response.data;
    },

    /**
     * Connexion d'un utilisateur
     */
    login: async (data: LoginRequest): Promise<AuthResponse> => {
        const response = await api.post<AuthResponse>('/auth/login', data);
        if (response.data.token) {
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('user', JSON.stringify({
                id: response.data.id,
                nom: response.data.nom,
                email: response.data.email,
            }));
        }
        return response.data;
    },

    /**
     * Mot de passe oublié
     */
    forgotPassword: async (email: string): Promise<string> => {
        const response = await api.post<string>('/auth/forgot-password', { email });
        return response.data;
    },

    /**
     * Réinitialiser le mot de passe
     */
    resetPassword: async (token: string, newPassword: string): Promise<string> => {
        const response = await api.post<string>('/auth/reset-password', { token, newPassword });
        return response.data;
    },

    /**
     * Déconnexion
     */
    logout: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    },

    /**
     * Récupère l'utilisateur courant depuis le localStorage
     */
    getCurrentUser: () => {
        const userStr = localStorage.getItem('user');
        return userStr ? JSON.parse(userStr) : null;
    },

    /**
     * Vérifie si l'utilisateur est connecté
     */
    isAuthenticated: (): boolean => {
        return !!localStorage.getItem('token');
    },
};
