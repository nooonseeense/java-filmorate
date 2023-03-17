package ru.application.filmorate.impl;

import ru.application.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> get();

    Film getById(Integer filmId);

    List<Film> getPopularMoviesByLikes(Integer count);

    List<Film> getCommonMovies(Integer userId, Integer friendId);

    Film add(Film film);

    Film update(Film film);

    void addGenres(Film film);

    void removeGenres(Film film);

    List<Film> getRecommendedFilms(Integer userId, List<Integer> matchingUserIds);

    /**
     * Метод удаления фильма по ID
     *
     * @param id id фильма
     */
    void removeFilmById(Integer id);
}