package ru.application.filmorate.impl;

import ru.application.filmorate.model.Film;

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

    Film add(Film film);

    Film update(Film film);

    void addGenres(Film film);

    void removeGenres(Film film);
}