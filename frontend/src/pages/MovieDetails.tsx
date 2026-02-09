import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Calendar, Star, Heart, Check, Play } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { filmService } from '../services/filmService';
import { favoriService } from '../services/favoriService';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';

const MovieDetails: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const { user } = useAuth();
    const [film, setFilm] = useState<any>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [isFavorite, setIsFavorite] = useState(false);
    const [addingFavorite, setAddingFavorite] = useState(false);

    const imageBaseUrl = 'https://image.tmdb.org/t/p';

    useEffect(() => {
        if (id) {
            loadFilmDetails();
            checkIfFavorite();
        }
    }, [id, user]);

    const loadFilmDetails = async () => {
        setLoading(true);
        setError('');

        try {
            const data = await filmService.getFilmDetails(Number(id), user?.id);
            setFilm(data);
        } catch (err: any) {
            setError('Erreur lors du chargement des détails du film');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const checkIfFavorite = async () => {
        if (!user || !id) return;

        try {
            const result = await favoriService.isFavori(user.id, Number(id));
            setIsFavorite(result);
        } catch (err) {
            console.error('Erreur vérification favori:', err);
        }
    };

    const handleAddToFavorites = async () => {
        if (!user || !id) return;

        setAddingFavorite(true);
        try {
            await favoriService.ajouterFavori({
                filmId: Number(id),
                utilisateurId: user.id,
            });
            setIsFavorite(true);
        } catch (err: any) {
            alert(err.response?.data?.message || 'Erreur lors de l\'ajout aux favoris');
        } finally {
            setAddingFavorite(false);
        }
    };

    if (loading) return <Loading />;
    if (error) return <div className="container mx-auto px-4 py-8"><ErrorMessage message={error} /></div>;
    if (!film) return null;

    const backdropUrl = film.backdrop_path ? `${imageBaseUrl}/original${film.backdrop_path}` : null;
    const posterUrl = film.poster_path ? `${imageBaseUrl}/w500${film.poster_path}` : null;

    return (
        <div className="min-h-screen bg-dark-900">
            {/* Backdrop */}
            {backdropUrl && (
                <div className="relative h-[60vh] overflow-hidden">
                    <img
                        src={backdropUrl}
                        alt={film.title}
                        className="w-full h-full object-cover"
                    />
                    <div className="gradient-overlay" />
                </div>
            )}

            {/* Content */}
            <div className="container mx-auto px-4 -mt-40 relative z-10">
                <div className="flex flex-col md:flex-row gap-8">
                    {/* Poster */}
                    {posterUrl && (
                        <div className="flex-shrink-0">
                            <img
                                src={posterUrl}
                                alt={film.title}
                                className="w-64 rounded-xl shadow-2xl"
                            />
                        </div>
                    )}

                    {/* Info */}
                    <div className="flex-1 text-white">
                        <h1 className="text-4xl md:text-5xl font-bold mb-4">{film.title}</h1>

                        <div className="flex flex-wrap items-center gap-4 mb-6">
                            {film.release_date && (
                                <div className="flex items-center space-x-1 text-gray-300">
                                    <Calendar className="w-5 h-5" />
                                    <span>{new Date(film.release_date).getFullYear()}</span>
                                </div>
                            )}

                            {film.vote_average && (
                                <div className="flex items-center space-x-1 bg-dark-800 px-3 py-1 rounded-lg">
                                    <Star className="w-5 h-5 text-yellow-400 fill-yellow-400" />
                                    <span className="font-semibold">{film.vote_average.toFixed(1)}</span>
                                </div>
                            )}

                            {film.genres && (
                                <div className="flex flex-wrap gap-2">
                                    {film.genres.map((genre: any) => (
                                        <span key={genre.id} className="bg-cinema-600 px-3 py-1 rounded-full text-sm">
                                            {genre.name}
                                        </span>
                                    ))}
                                </div>
                            )}
                        </div>

                        {/* Actions */}
                        <div className="flex gap-4 mb-8">
                            {!isFavorite ? (
                                <button
                                    onClick={handleAddToFavorites}
                                    disabled={addingFavorite || !user}
                                    className="btn-primary flex items-center space-x-2 disabled:opacity-50"
                                >
                                    <Heart className="w-5 h-5" />
                                    <span>{addingFavorite ? 'Ajout...' : 'Ajouter aux favoris'}</span>
                                </button>
                            ) : (
                                <div className="flex items-center space-x-2 text-green-400">
                                    <Check className="w-5 h-5" />
                                    <span>Dans vos favoris</span>
                                </div>
                            )}
                        </div>

                        {/* Synopsis */}
                        {film.overview && (
                            <div className="mb-8">
                                <h2 className="text-2xl font-bold mb-3">Synopsis</h2>
                                <p className="text-gray-300 leading-relaxed">{film.overview}</p>
                            </div>
                        )}

                        {/* Casting */}
                        {film.credits?.cast && film.credits.cast.length > 0 && (
                            <div className="mb-8">
                                <h2 className="text-2xl font-bold mb-3">Casting</h2>
                                <div className="flex flex-wrap gap-3">
                                    {film.credits.cast.slice(0, 10).map((actor: any) => (
                                        <div key={actor.id} className="text-gray-300">
                                            {actor.name}
                                        </div>
                                    ))}
                                </div>
                            </div>
                        )}

                        {/* Director */}
                        {film.credits?.crew && (
                            <>
                                {film.credits.crew.filter((c: any) => c.job === 'Director').map((director: any) => (
                                    <div key={director.id} className="mb-4">
                                        <span className="text-gray-400">Réalisateur: </span>
                                        <span className="text-white font-semibold">{director.name}</span>
                                    </div>
                                ))}
                            </>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default MovieDetails;
