package ru.application.filmorate.impl;

import ru.application.filmorate.model.Film;
import ru.application.filmorate.model.enums.FilmSort;

import java.util.List;

public interface FilmStorage {
    List<Film> get();

    Film getById(Integer filmId);

    List<Film> getPopularMoviesByLikes(Integer count);

    /**
     * Метод расширенного поиска списка фильмов по определённым параметрам
     *
     * @param query текст для поиска
     * @param by    может принимать значения director (поиск по режиссёру), title (поиск по названию)
     * @return Список фильмов
     */
    List<Film> getPopularMoviesFromAdvancedSearch(String query, String by);

    List<Film> getCommonMovies(Integer userId, Integer friendId);

    List<Film> getBy(int directorId, FilmSort sortBy);

    Film add(Film film);

    Film update(Film film);

    void addGenres(Film film);

    void addDirectors(Film film);

    void removeGenres(Film film);

    void removeDirectors(Film film);

    List<Film> getRecommendedFilms(Integer userId);

    void removeFilmById(Integer id);
}