import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Eye, EyeOff, Trash2, Calendar } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { favoriService } from '../services/favoriService';
import { Favori } from '../types/types';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';

const Favorites: React.FC = () => {
    const { user } = useAuth();
    const navigate = useNavigate();
    const [favoris, setFavoris] = useState<Favori[]>([]);
    const [filter, setFilter] = useState<'all' | 'vu' | 'non-vu'>('all');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    const imageBaseUrl = 'https://image.tmdb.org/t/p/w500';

    useEffect(() => {
        if (user) {
            loadFavoris();
        }
    }, [user, filter]);

    const loadFavoris = async () => {
        if (!user) return;

        setLoading(true);
        setError('');

        try {
            let data: Favori[];
            if (filter === 'vu') {
                data = await favoriService.getFavoris(user.id, true);
            } else if (filter === 'non-vu') {
                data = await favoriService.getFavoris(user.id, false);
            } else {
                data = await favoriService.getFavoris(user.id);
            }
            setFavoris(data);
        } catch (err: any) {
            setError('Erreur lors du chargement des favoris');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleToggleVu = async (favori: Favori) => {
        try {
            await favoriService.updateVu(favori.id, { vu: !favori.vu });
            loadFavoris();
        } catch (err) {
            alert('Erreur lors de la mise à jour');
        }
    };

    const handleDelete = async (favoriId: number) => {
        if (!confirm('Êtes-vous sûr de vouloir retirer ce film de vos favoris ?')) return;

        try {
            await favoriService.supprimerFavori(favoriId);
            loadFavoris();
        } catch (err) {
            alert('Erreur lors de la suppression');
        }
    };

    if (loading) return <Loading />;

    return (
        <div className="min-h-screen bg-dark-900">
            <div className="container mx-auto px-4 py-8">
                <h1 className="text-4xl font-bold text-white mb-8">Mes Favoris</h1>

                {/* Filters */}
                <div className="flex gap-4 mb-8">
                    <button
                        onClick={() => setFilter('all')}
                        className={`px-4 py-2 rounded-lg font-semibold transition-colors ${filter === 'all' ? 'bg-cinema-600 text-white' : 'bg-dark-700 text-gray-300 hover:bg-dark-600'
                            }`}
                    >
                        Tous
                    </button>
                    <button
                        onClick={() => setFilter('vu')}
                        className={`px-4 py-2 rounded-lg font-semibold transition-colors ${filter === 'vu' ? 'bg-cinema-600 text-white' : 'bg-dark-700 text-gray-300 hover:bg-dark-600'
                            }`}
                    >
                        Vus
                    </button>
                    <button
                        onClick={() => setFilter('non-vu')}
                        className={`px-4 py-2 rounded-lg font-semibold transition-colors ${filter === 'non-vu' ? 'bg-cinema-600 text-white' : 'bg-dark-700 text-gray-300 hover:bg-dark-600'
                            }`}
                    >
                        À voir
                    </button>
                </div>

                {error && <ErrorMessage message={error} />}

                {/* List */}
                {favoris.length > 0 ? (
                    <div className="grid gap-6">
                        {favoris.map((favori) => (
                            <div key={favori.id} className="bg-dark-800 rounded-xl overflow-hidden flex">
                                {/* Poster */}
                                <div
                                    onClick={() => navigate(`/movie/${favori.film.id}`)}
                                    className="w-32 flex-shrink-0 cursor-pointer"
                                >
                                    {favori.film.posterPath ? (
                                        <img
                                            src={`${imageBaseUrl}${favori.film.posterPath}`}
                                            alt={favori.film.titre}
                                            className="w-full h-full object-cover hover:opacity-80 transition-opacity"
                                        />
                                    ) : (
                                        <div className="w-full h-full bg-dark-700 flex items-center justify-center text-gray-500">
                                            Pas d'image
                                        </div>
                                    )}
                                </div>

                                {/* Info */}
                                <div className="flex-1 p-6 flex flex-col justify-between">
                                    <div>
                                        <h3
                                            onClick={() => navigate(`/movie/${favori.film.id}`)}
                                            className="text-xl font-bold text-white mb-2 cursor-pointer hover:text-cinema-400 transition-colors"
                                        >
                                            {favori.film.titre}
                                        </h3>
                                        {favori.film.anneeSortie && (
                                            <div className="flex items-center space-x-1 text-gray-400 text-sm mb-3">
                                                <Calendar className="w-4 h-4" />
                                                <span>{favori.film.anneeSortie}</span>
                                            </div>
                                        )}
                                        {favori.commentaire && (
                                            <p className="text-gray-300 text-sm mb-3">{favori.commentaire}</p>
                                        )}
                                    </div>

                                    {/* Actions */}
                                    <div className="flex items-center gap-4">
                                        <button
                                            onClick={() => handleToggleVu(favori)}
                                            className={`flex items-center space-x-2 px-4 py-2 rounded-lg font-semibold transition-colors ${favori.vu
                                                    ? 'bg-green-600 hover:bg-green-700 text-white'
                                                    : 'bg-dark-700 hover:bg-dark-600 text-gray-300'
                                                }`}
                                        >
                                            {favori.vu ? <Eye className="w-4 h-4" /> : <EyeOff className="w-4 h-4" />}
                                            <span>{favori.vu ? 'Vu' : 'Marquer comme vu'}</span>
                                        </button>

                                        <button
                                            onClick={() => handleDelete(favori.id)}
                                            className="flex items-center space-x-2 px-4 py-2 rounded-lg bg-red-600 hover:bg-red-700 text-white font-semibold transition-colors"
                                        >
                                            <Trash2 className="w-4 h-4" />
                                            <span>Retirer</span>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                ) : (
                    <div className="text-center py-12">
                        <p className="text-gray-400 text-lg">Aucun favori pour le moment.</p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default Favorites;
