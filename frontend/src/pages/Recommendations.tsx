import React, { useEffect, useState } from 'react';
import { Sparkles } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { userService } from '../services/userService';
import { TmdbMovie } from '../types/types';
import FilmCard from '../components/FilmCard';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';

const Recommendations: React.FC = () => {
    const { user } = useAuth();
    const [recommendations, setRecommendations] = useState<TmdbMovie[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        if (user) {
            loadRecommendations();
        }
    }, [user]);

    const loadRecommendations = async () => {
        if (!user) return;

        setLoading(true);
        setError('');

        try {
            const data = await userService.getRecommandations(user.id);
            setRecommendations(data);
        } catch (err: any) {
            setError('Erreur lors du chargement des recommandations');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    if (loading) return <Loading />;

    return (
        <div className="min-h-screen bg-dark-900">
            <div className="container mx-auto px-4 py-8">
                <div className="mb-8">
                    <h1 className="text-4xl font-bold text-white mb-2 flex items-center space-x-3">
                        <Sparkles className="w-10 h-10 text-cinema-500" />
                        <span>Recommandations pour vous</span>
                    </h1>
                    <p className="text-gray-400">
                        Basées sur vos favoris, préférences et historique de recherche
                    </p>
                </div>

                {error && <ErrorMessage message={error} />}

                {recommendations.length > 0 ? (
                    <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
                        {recommendations.map((film) => (
                            <FilmCard key={film.id} film={film} />
                        ))}
                    </div>
                ) : (
                    <div className="text-center py-12">
                        <p className="text-gray-400 text-lg mb-4">
                            Aucune recommandation disponible pour le moment.
                        </p>
                        <p className="text-gray-500">
                            Ajoutez des films à vos favoris et définissez vos préférences de genres pour obtenir des recommandations personnalisées.
                        </p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default Recommendations;
