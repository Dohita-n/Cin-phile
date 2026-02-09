import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Star, Calendar } from 'lucide-react';
import { TmdbMovie } from '../types/types';

interface FilmCardProps {
    film: TmdbMovie;
}

const FilmCard: React.FC<FilmCardProps> = ({ film }) => {
    const navigate = useNavigate();
    const imageBaseUrl = 'https://image.tmdb.org/t/p/w500';

    const handleClick = () => {
        navigate(`/movie/${film.id}`);
    };

    return (
        <div
            onClick={handleClick}
            className="card cursor-pointer group"
        >
            {/* Image */}
            <div className="relative aspect-[2/3] overflow-hidden bg-dark-700">
                {film.poster_path ? (
                    <img
                        src={`${imageBaseUrl}${film.poster_path}`}
                        alt={film.title}
                        className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-300"
                    />
                ) : (
                    <div className="w-full h-full flex items-center justify-center text-gray-500">
                        <span>Pas d'image</span>
                    </div>
                )}

                {/* Note overlay */}
                {film.vote_average && (
                    <div className="absolute top-2 right-2 bg-dark-900/90 backdrop-blur-sm rounded-lg px-2 py-1 flex items-center space-x-1">
                        <Star className="w-4 h-4 text-yellow-400 fill-yellow-400" />
                        <span className="text-sm font-semibold">{film.vote_average.toFixed(1)}</span>
                    </div>
                )}
            </div>

            {/* Info */}
            <div className="p-4">
                <h3 className="font-semibold text-white line-clamp-2 mb-2 group-hover:text-cinema-400 transition-colors">
                    {film.title}
                </h3>

                {film.release_date && (
                    <div className="flex items-center space-x-1 text-gray-400 text-sm">
                        <Calendar className="w-4 h-4" />
                        <span>{new Date(film.release_date).getFullYear()}</span>
                    </div>
                )}
            </div>
        </div>
    );
};

export default FilmCard;
