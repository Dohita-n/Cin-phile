import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { User, AuthResponse, LoginRequest, RegisterRequest } from '../types/types';
import { authService } from '../services/authService';

interface AuthContextType {
    user: User | null;
    login: (data: LoginRequest) => Promise<void>;
    register: (data: RegisterRequest) => Promise<void>;
    logout: () => void;
    isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [user, setUser] = useState<User | null>(null);

    useEffect(() => {
        // Charger l'utilisateur depuis le localStorage au démarrage
        const currentUser = authService.getCurrentUser();
        if (currentUser) {
            setUser(currentUser);
        }
    }, []);

    const login = async (data: LoginRequest) => {
        const response: AuthResponse = await authService.login(data);
        setUser({
            id: response.id,
            nom: response.nom,
            email: response.email,
        });
    };

    const register = async (data: RegisterRequest) => {
        const response: AuthResponse = await authService.register(data);
        setUser({
            id: response.id,
            nom: response.nom,
            email: response.email,
        });
    };

    const logout = () => {
        authService.logout();
        setUser(null);
    };

    const value = {
        user,
        login,
        register,
        logout,
        isAuthenticated: !!user,
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};
