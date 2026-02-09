import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { filmService } from '../services/filmService';
import { TmdbMovie, PersonWithFilms, UniversalSearchResult, SearchFilters } from '../types/types';
import FilmCard from '../components/FilmCard';
import SearchBar from '../components/SearchBar';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';
import { Film, Video, User, ChevronDown, ChevronUp, Search as SearchIcon } from 'lucide-react';
import { Link } from 'react-router-dom';

const Search: React.FC = () => {
    const { user } = useAuth();
    const location = useLocation();
    const initialFilters = location.state?.filters || {};

    const [results, setResults] = useState<UniversalSearchResult | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [searchedQuery, setSearchedQuery] = useState('');

    // Accordion states
    const [actorsExpanded, setActorsExpanded] = useState(true);
    const [directorsExpanded, setDirectorsExpanded] = useState(true);

    useEffect(() => {
        if (Object.keys(initialFilters).length > 0) {
            handleSearch(initialFilters);
        }
    }, []);

    const handleSearch = async (filters: SearchFilters) => {
        if (!filters.query || filters.query.trim().length < 3) {
            return;
        }

        setLoading(true);
        setError('');
        setSearchedQuery(filters.query);

        try {
            const data = await filmService.universalSearch(
                filters.query,
                user?.id,
                filters.genre,
                filters.annee
            );
            setResults(data);
        } catch (err) {
            console.error(err);
            setError('Une erreur est survenue lors de la recherche.');
        } finally {
            setLoading(false);
        }
    };

    // Helper to render person section (Actor/Director)
    const renderPersonSection = (
        title: string,
        people: PersonWithFilms[] | undefined,
        icon: React.ReactNode,
        isExpanded: boolean,
        toggleExpand: () => void
    ) => {
        if (!people || people.length === 0) return null;

        return (
            <div className="mb-8 bg-dark-800 rounded-xl overflow-hidden border border-dark-700">
                <button
                    onClick={toggleExpand}
                    className="w-full flex items-center justify-between p-4 bg-dark-750 hover:bg-dark-700 transition-colors"
                >
                    <div className="flex items-center space-x-3">
                        {icon}
                        <h2 className="text-xl font-bold text-white">
                            {title} <span className="text-gray-400 text-sm ml-2">({people.length})</span>
                        </h2>
                    </div>
                    {isExpanded ? <ChevronUp className="text-gray-400" /> : <ChevronDown className="text-gray-400" />}
                </button>

                {isExpanded && (
                    <div className="p-4 space-y-6">
                        {people.map((person) => (
                            <div key={person.id} className="border-b border-dark-700 last:border-0 pb-6 last:pb-0">
                                <div className="flex items-center space-x-4 mb-4">
                                    {person.profilePath ? (
                                        <img
                                            src={`https://image.tmdb.org/t/p/w200${person.profilePath}`}
                                            alt={person.name}
                                            className="w-16 h-16 rounded-full object-cover border-2 border-cinema-500"
                                        />
                                    ) : (
                                        <div className="w-16 h-16 rounded-full bg-dark-700 flex items-center justify-center text-gray-400">
                                            <User size={24} />
                                        </div>
                                    )}
                                    <div>
                                        <h3 className="text-lg font-bold text-white">{person.name}</h3>
                                        <p className="text-sm text-gray-400">
                                            {person.films.length} film{person.films.length > 1 ? 's' : ''} associé{person.films.length > 1 ? 's' : ''}
                                        </p>
                                    </div>
                                </div>

                                <div className="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-5 gap-4">
                                    {person.films.map((film) => (
                                        <Link to={`/film/${film.id}`} key={film.id} className="block group">
                                            <div className="relative aspect-[2/3] rounded-lg overflow-hidden bg-dark-800 mb-2">
                                                {film.poster_path ? (
                                                    <img
                                                        src={`https://image.tmdb.org/t/p/w300${film.poster_path}`}
                                                        alt={film.title}
                                                        className="w-full h-full object-cover transform group-hover:scale-105 transition-transform duration-300"
                                                    />
                                                ) : (
                                                    <div className="w-full h-full flex items-center justify-center text-gray-600">
                                                        <Video size={32} />
                                                    </div>
                                                )}
                                                <div className="absolute inset-0 bg-black/60 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center">
                                                    <span className="text-white font-bold text-sm text-center px-2">{film.title}</span>
                                                </div>
                                            </div>
                                            <p className="text-xs text-gray-400 truncate group-hover:text-cinema-400 transition-colors">
                                                {film.title}
                                            </p>
                                        </Link>
                                    ))}
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        );
    };

    return (
        <div className="min-h-screen bg-dark-900">
            {/* Hero Section - Identical to Home */}
            <div className="relative bg-gradient-to-b from-cinema-900/30 to-dark-900 py-16">
                <div className="container mx-auto px-4">
                    <div className="max-w-3xl mx-auto text-center mb-12">
                        <h1 className="text-5xl font-bold text-white mb-4">
                            Recherche Universelle
                        </h1>
                        <p className="text-xl text-gray-300">
                            Films, acteurs et réalisateurs en un seul endroit
                        </p>
                    </div>

                    {/* Search Bar */}
                    <div className="max-w-4xl mx-auto">
                        <SearchBar onSearch={handleSearch} />
                    </div>
                </div>
            </div>

            <div className="container mx-auto px-4 py-12">
                {/* Error Message */}
                {error && <ErrorMessage message={error} />}

                {/* Loading State */}
                {loading && <Loading />}

                {/* Results Area */}
                {!loading && results && (
                    <div className="space-y-10">

                        {/* Empty State */}
                        {(!results.films?.results.length && !results.actors?.length && !results.directors?.length) && (
                            <div className="text-center py-12">
                                <div className="inline-block p-6 bg-dark-800 rounded-full mb-4">
                                    <SearchIcon className="h-12 w-12 text-gray-600" />
                                </div>
                                <h3 className="text-xl font-bold text-gray-300">Aucun résultat trouvé</h3>
                                <p className="text-gray-500 mt-2">Nous n'avons rien trouvé pour "{searchedQuery}". Essayez un autre terme ou ajustez les filtres.</p>
                            </div>
                        )}

                        {/* 1. Films Section */}
                        {results.films?.results && results.films.results.length > 0 && (
                            <section>
                                <div className="flex items-center space-x-3 mb-6">
                                    <Film className="h-8 w-8 text-cinema-500" />
                                    <h2 className="text-3xl font-bold text-white">
                                        Films <span className="text-gray-500 text-xl ml-2">({results.films.results.length})</span>
                                    </h2>
                                </div>
                                <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-6 gap-6">
                                    {results.films.results.map((film: TmdbMovie) => (
                                        <FilmCard key={film.id} film={film} />
                                    ))}
                                </div>
                            </section>
                        )}

                        {/* 2. Actors Section */}
                        {results.actors && results.actors.length > 0 && (
                            renderPersonSection(
                                "Acteurs",
                                results.actors,
                                <User className="h-8 w-8 text-purple-500" />,
                                actorsExpanded,
                                () => setActorsExpanded(!actorsExpanded)
                            )
                        )}

                        {/* 3. Directors Section */}
                        {results.directors && results.directors.length > 0 && (
                            renderPersonSection(
                                "Réalisateurs",
                                results.directors,
                                <Video className="h-8 w-8 text-pink-500" />,
                                directorsExpanded,
                                () => setDirectorsExpanded(!directorsExpanded)
                            )
                        )}
                    </div>
                )}
            </div>
        </div>
    );
};

export default Search;
