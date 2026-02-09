import React, { useEffect, useState } from 'react';
import { Settings } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { filmService } from '../services/filmService';
import { userService } from '../services/userService';
import { Genre } from '../types/types';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';

const Profile: React.FC = () => {
    const { user } = useAuth();
    const [genres, setGenres] = useState<Genre[]>([]);
    const [selectedGenres, setSelectedGenres] = useState<number[]>([]);
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState(false);

    useEffect(() => {
        loadData();
    }, [user]);

    const loadData = async () => {
        if (!user) return;

        setLoading(true);
        setError('');

        try {
            // Charger tous les genres
            const allGenres = await filmService.getGenres();
            setGenres(allGenres);

            // Charger les préférences de l'utilisateur
            const userPreferences = await userService.getPreferences(user.id);
            setSelectedGenres(userPreferences.map((g) => g.id));
        } catch (err: any) {
            setError('Erreur lors du chargement des données');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleToggleGenre = (genreId: number) => {
        setSelectedGenres((prev) =>
            prev.includes(genreId) ? prev.filter((id) => id !== genreId) : [...prev, genreId]
        );
    };

    const handleSave = async () => {
        if (!user) return;

        setSaving(true);
        setError('');
        setSuccess(false);

        try {
            await userService.updatePreferences(user.id, selectedGenres);
            setSuccess(true);
            setTimeout(() => setSuccess(false), 3000);
        } catch (err: any) {
            setError('Erreur lors de la sauvegarde des préférences');
        } finally {
            setSaving(false);
        }
    };

    if (loading) return <Loading />;

    return (
        <div className="min-h-screen bg-dark-900">
            <div className="container mx-auto px-4 py-8">
                <div className="max-w-4xl mx-auto">
                    <div className="mb-8">
                        <h1 className="text-4xl font-bold text-white mb-2 flex items-center space-x-3">
                            <Settings className="w-10 h-10 text-cinema-500" />
                            <span>Mon Profil</span>
                        </h1>
                        <p className="text-gray-400">Gérez vos informations et préférences</p>
                    </div>

                    {/* User Info */}
                    <div className="bg-dark-800 rounded-xl p-6 mb-8 border border-dark-700">
                        <h2 className="text-2xl font-bold text-white mb-4">Informations</h2>
                        <div className="space-y-3">
                            <div>
                                <span className="text-gray-400">Nom: </span>
                                <span className="text-white font-semibold">{user?.nom}</span>
                            </div>
                            <div>
                                <span className="text-gray-400">Email: </span>
                                <span className="text-white font-semibold">{user?.email}</span>
                            </div>
                        </div>
                    </div>

                    {/* Genre Preferences */}
                    <div className="bg-dark-800 rounded-xl p-6 border border-dark-700">
                        <h2 className="text-2xl font-bold text-white mb-4">Préférences de genres</h2>
                        <p className="text-gray-400 mb-6">
                            Sélectionnez vos genres préférés pour obtenir de meilleures recommandations
                        </p>

                        {error && <div className="mb-4"><ErrorMessage message={error} /></div>}

                        {success && (
                            <div className="mb-4 bg-green-900/20 border border-green-500/50 rounded-lg p-4">
                                <p className="text-green-400">Préférences sauvegardées avec succès !</p>
                            </div>
                        )}

                        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-3 mb-6">
                            {genres.map((genre) => (
                                <button
                                    key={genre.id}
                                    onClick={() => handleToggleGenre(genre.id)}
                                    className={`px-4 py-3 rounded-lg font-semibold transition-all ${selectedGenres.includes(genre.id)
                                            ? 'bg-cinema-600 text-white shadow-lg scale-105'
                                            : 'bg-dark-700 text-gray-300 hover:bg-dark-600'
                                        }`}
                                >
                                    {genre.libelle}
                                </button>
                            ))}
                        </div>

                        <button
                            onClick={handleSave}
                            disabled={saving}
                            className="btn-primary disabled:opacity-50 disabled:cursor-not-allowed"
                        >
                            {saving ? 'Sauvegarde...' : 'Sauvegarder les préférences'}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Profile;
