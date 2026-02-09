import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { TrendingUp, Star } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { filmService } from '../services/filmService';
import { userService } from '../services/userService';
import { TmdbMovie } from '../types/types';
import FilmCard from '../components/FilmCard';
import SearchBar from '../components/SearchBar';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';
import { SearchFilters } from '../types/types';

const Home: React.FC = () => {
    const { user } = useAuth();
    const navigate = useNavigate();
    const [popularFilms, setPopularFilms] = useState<TmdbMovie[]>([]);
    const [recommendations, setRecommendations] = useState<TmdbMovie[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        loadData();
    }, [user]);

    const loadData = async () => {
        setLoading(true);
        setError('');

        try {
            // Charger les films populaires
            const popularData = await filmService.getPopularFilms(1);
            setPopularFilms(popularData.results.slice(0, 12));

            // Charger les recommandations si l'utilisateur est connecté
            if (user) {
                try {
                    const recoData = await userService.getRecommandations(user.id);
                    setRecommendations(recoData.slice(0, 12));
                } catch (err) {
                    console.error('Erreur chargement recommandations:', err);
                }
            }
        } catch (err: any) {
            setError('Erreur lors du chargement des films');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleSearch = (filters: SearchFilters) => {
        navigate('/search', { state: { filters } });
    };

    if (loading) return <Loading />;

    return (
        <div className="min-h-screen bg-dark-900">
            {/* Hero Section */}
            <div className="relative bg-gradient-to-b from-cinema-900/30 to-dark-900 py-16">
                <div className="container mx-auto px-4">
                    <div className="max-w-3xl mx-auto text-center mb-12">
                        <h1 className="text-5xl font-bold text-white mb-4">
                            Découvrez votre prochain film préféré
                        </h1>
                        <p className="text-xl text-gray-300">
                            Recherchez, sauvegardez et obtenez des recommandations personnalisées
                        </p>
                    </div>

                    {/* Search Bar */}
                    <div className="max-w-4xl mx-auto">
                        <SearchBar onSearch={handleSearch} />
                    </div>
                </div>
            </div>

            {/* Content */}
            <div className="container mx-auto px-4 py-12">
                {error && <ErrorMessage message={error} />}

                {/* Recommandations personnalisées */}
                {user && recommendations.length > 0 && (
                    <section className="mb-16">
                        <div className="flex items-center justify-between mb-6">
                            <h2 className="text-3xl font-bold text-white flex items-center space-x-2">
                                <Star className="w-8 h-8 text-cinema-500" />
                                <span>Recommandations pour vous</span>
                            </h2>
                            <button
                                onClick={() => navigate('/recommendations')}
                                className="text-cinema-400 hover:text-cinema-300 font-semibold"
                            >
                                Voir tout →
                            </button>
                        </div>
                        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-6 gap-6">
                            {recommendations.map((film) => (
                                <FilmCard key={film.id} film={film} />
                            ))}
                        </div>
                    </section>
                )}

                {/* Films populaires */}
                <section>
                    <div className="flex items-center justify-between mb-6">
                        <h2 className="text-3xl font-bold text-white flex items-center space-x-2">
                            <TrendingUp className="w-8 h-8 text-cinema-500" />
                            <span>Films populaires</span>
                        </h2>
                    </div>
                    <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-6 gap-6">
                        {popularFilms.map((film) => (
                            <FilmCard key={film.id} film={film} />
                        ))}
                    </div>
                </section>
            </div>
        </div>
    );
};

export default Home;
