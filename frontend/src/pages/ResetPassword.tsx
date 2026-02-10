import React, { useState } from 'react';
import { useNavigate, useSearchParams, Link } from 'react-router-dom';
import { Lock, ArrowRight, CheckCircle, Film } from 'lucide-react';
import { authService } from '../services/authService';
import ErrorMessage from '../components/ErrorMessage';

const ResetPassword: React.FC = () => {
    const [searchParams] = useSearchParams();
    const token = searchParams.get('token');
    const navigate = useNavigate();

    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setMessage('');

        if (newPassword !== confirmPassword) {
            setError('Les mots de passe ne correspondent pas');
            return;
        }

        if (newPassword.length < 6) {
            setError('Le mot de passe doit contenir au moins 6 caractères');
            return;
        }

        if (!token) {
            setError('Token invalide ou manquant');
            return;
        }

        setLoading(true);

        try {
            const response = await authService.resetPassword(token, newPassword);
            setMessage(response || 'Mot de passe modifié avec succès !');
            setTimeout(() => {
                navigate('/login');
            }, 3000);
        } catch (err: any) {
            setError(err.response?.data?.message || 'Erreur lors de la réinitialisation du mot de passe');
        } finally {
            setLoading(false);
        }
    };

    if (!token) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-dark-900 px-4">
                <div className="text-center text-white">
                    <h2 className="text-2xl font-bold mb-4">Lien invalide</h2>
                    <p className="text-gray-400 mb-6">Le lien de réinitialisation est invalide ou manquant.</p>
                    <Link to="/login" className="btn-primary">Retour à la connexion</Link>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-dark-900 via-dark-800 to-cinema-900/20 px-4">
            <div className="max-w-md w-full">
                {/* Logo */}
                <div className="text-center mb-8">
                    <div className="flex items-center justify-center space-x-2 text-cinema-500 mb-2">
                        <Film className="w-12 h-12" />
                        <span className="text-3xl font-bold">Cinéphile</span>
                    </div>
                </div>

                <div className="bg-dark-800 rounded-xl shadow-2xl p-8 border border-dark-700">
                    <h2 className="text-2xl font-bold text-white mb-6 text-center">Réinitialiser le mot de passe</h2>

                    {error && <div className="mb-4"><ErrorMessage message={error} /></div>}

                    {message && (
                        <div className="mb-6 p-4 bg-green-500/10 border border-green-500/20 text-green-400 rounded-lg flex items-center justify-center flex-col">
                            <CheckCircle className="w-12 h-12 mb-2" />
                            <p className="text-center">{message}</p>
                            <p className="text-sm mt-2 text-gray-400">Redirection vers la connexion...</p>
                        </div>
                    )}

                    {!message && (
                        <form onSubmit={handleSubmit} className="space-y-6">
                            <div>
                                <label htmlFor="newPassword" className="block text-sm font-medium text-gray-300 mb-2">
                                    Nouveau mot de passe
                                </label>
                                <div className="relative">
                                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                        <Lock className="h-5 w-5 text-gray-500" />
                                    </div>
                                    <input
                                        id="newPassword"
                                        type="password"
                                        value={newPassword}
                                        onChange={(e) => setNewPassword(e.target.value)}
                                        required
                                        minLength={6}
                                        className="input-field pl-10"
                                        placeholder="••••••••"
                                    />
                                </div>
                            </div>

                            <div>
                                <label htmlFor="confirmPassword" className="block text-sm font-medium text-gray-300 mb-2">
                                    Confirmer le mot de passe
                                </label>
                                <div className="relative">
                                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                        <Lock className="h-5 w-5 text-gray-500" />
                                    </div>
                                    <input
                                        id="confirmPassword"
                                        type="password"
                                        value={confirmPassword}
                                        onChange={(e) => setConfirmPassword(e.target.value)}
                                        required
                                        className="input-field pl-10"
                                        placeholder="••••••••"
                                    />
                                </div>
                            </div>

                            <button
                                type="submit"
                                disabled={loading}
                                className="w-full btn-primary flex items-center justify-center space-x-2 disabled:opacity-50 disabled:cursor-not-allowed"
                            >
                                <ArrowRight className="w-5 h-5" />
                                <span>{loading ? 'Modification...' : 'Modifier le mot de passe'}</span>
                            </button>
                        </form>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ResetPassword;
