import React, { useState, useEffect } from 'react';
import { Search, Filter } from 'lucide-react';
import { Genre, SearchFilters } from '../types/types';
import { filmService } from '../services/filmService';

interface SearchBarProps {
    onSearch: (filters: SearchFilters) => void;
}

const SearchBar: React.FC<SearchBarProps> = ({ onSearch }) => {
    const [query, setQuery] = useState('');
    const [selectedGenre, setSelectedGenre] = useState<number | undefined>();
    const [selectedYear, setSelectedYear] = useState<number | undefined>();
    const [genres, setGenres] = useState<Genre[]>([]);
    const [showFilters, setShowFilters] = useState(false);

    useEffect(() => {
        loadGenres();
    }, []);

    const loadGenres = async () => {
        try {
            const data = await filmService.getGenres();
            setGenres(data);
        } catch (error) {
            console.error('Erreur chargement genres:', error);
        }
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onSearch({
            query: query || undefined,
            genre: selectedGenre,
            annee: selectedYear,
        });
    };

    const currentYear = new Date().getFullYear();
    const years = Array.from({ length: 50 }, (_, i) => currentYear - i);

    return (
        <div className="w-full">
            <form onSubmit={handleSubmit} className="space-y-4">
                {/* Barre de recherche principale */}
                <div className="flex gap-2">
                    <div className="flex-1 relative">
                        <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                        <input
                            type="text"
                            value={query}
                            onChange={(e) => setQuery(e.target.value)}
                            placeholder="Rechercher un film..."
                            className="input-field pl-10"
                        />
                    </div>
                    <button
                        type="button"
                        onClick={() => setShowFilters(!showFilters)}
                        className="btn-secondary flex items-center space-x-2"
                    >
                        <Filter className="w-5 h-5" />
                        <span className="hidden md:inline">Filtres</span>
                    </button>
                    <button type="submit" className="btn-primary px-6">
                        Rechercher
                    </button>
                </div>

                {/* Filtres avancés */}
                {showFilters && (
                    <div className="bg-dark-800 rounded-lg p-4 space-y-4 border border-dark-700">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            {/* Filtre par genre */}
                            <div>
                                <label className="block text-sm font-medium text-gray-300 mb-2">
                                    Genre
                                </label>
                                <select
                                    value={selectedGenre || ''}
                                    onChange={(e) => setSelectedGenre(e.target.value ? Number(e.target.value) : undefined)}
                                    className="input-field"
                                >
                                    <option value="">Tous les genres</option>
                                    {genres.map((genre) => (
                                        <option key={genre.id} value={genre.id}>
                                            {genre.libelle}
                                        </option>
                                    ))}
                                </select>
                            </div>

                            {/* Filtre par année */}
                            <div>
                                <label className="block text-sm font-medium text-gray-300 mb-2">
                                    Année
                                </label>
                                <select
                                    value={selectedYear || ''}
                                    onChange={(e) => setSelectedYear(e.target.value ? Number(e.target.value) : undefined)}
                                    className="input-field"
                                >
                                    <option value="">Toutes les années</option>
                                    {years.map((year) => (
                                        <option key={year} value={year}>
                                            {year}
                                        </option>
                                    ))}
                                </select>
                            </div>
                        </div>

                        {/* Bouton réinitialiser */}
                        <button
                            type="button"
                            onClick={() => {
                                setQuery('');
                                setSelectedGenre(undefined);
                                setSelectedYear(undefined);
                                onSearch({});
                            }}
                            className="text-sm text-cinema-400 hover:text-cinema-300 transition-colors"
                        >
                            Réinitialiser les filtres
                        </button>
                    </div>
                )}
            </form>
        </div>
    );
};

export default SearchBar;
