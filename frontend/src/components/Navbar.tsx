import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Film, Heart, Home, LogOut, Search, Star, User } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

const Navbar: React.FC = () => {
    const { user, logout, isAuthenticated } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <nav className="bg-dark-800 border-b border-dark-700 sticky top-0 z-50">
            <div className="container mx-auto px-4">
                <div className="flex items-center justify-between h-16">
                    {/* Logo */}
                    <Link to="/" className="flex items-center space-x-2 text-cinema-500 hover:text-cinema-400 transition-colors">
                        <Film className="w-8 h-8" />
                        <span className="text-xl font-bold">Cinéphile</span>
                    </Link>

                    {/* Navigation Links */}
                    {isAuthenticated && (
                        <div className="hidden md:flex items-center space-x-6">
                            <Link
                                to="/"
                                className="flex items-center space-x-1 text-gray-300 hover:text-white transition-colors"
                            >
                                <Home className="w-5 h-5" />
                                <span>Accueil</span>
                            </Link>
                            <Link
                                to="/search"
                                className="flex items-center space-x-1 text-gray-300 hover:text-white transition-colors"
                            >
                                <Search className="w-5 h-5" />
                                <span>Recherche</span>
                            </Link>
                            <Link
                                to="/favorites"
                                className="flex items-center space-x-1 text-gray-300 hover:text-white transition-colors"
                            >
                                <Heart className="w-5 h-5" />
                                <span>Mes Favoris</span>
                            </Link>
                            <Link
                                to="/recommendations"
                                className="flex items-center space-x-1 text-gray-300 hover:text-white transition-colors"
                            >
                                <Star className="w-5 h-5" />
                                <span>Recommandations</span>
                            </Link>
                        </div>
                    )}

                    {/* User Menu */}
                    <div className="flex items-center space-x-4">
                        {isAuthenticated ? (
                            <>
                                <Link
                                    to="/profile"
                                    className="flex items-center space-x-2 text-gray-300 hover:text-white transition-colors"
                                >
                                    <User className="w-5 h-5" />
                                    <span className="hidden md:inline">{user?.nom}</span>
                                </Link>
                                <button
                                    onClick={handleLogout}
                                    className="flex items-center space-x-1 text-gray-300 hover:text-cinema-500 transition-colors"
                                >
                                    <LogOut className="w-5 h-5" />
                                    <span className="hidden md:inline">Déconnexion</span>
                                </button>
                            </>
                        ) : (
                            <Link to="/login" className="btn-primary">
                                Connexion
                            </Link>
                        )}
                    </div>
                </div>
            </div>
        </nav>
    );
};

export default Navbar;
