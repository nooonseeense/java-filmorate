package ru.application.filmorate.impl;

import ru.application.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> get();

    Film getById(Integer filmId);

    List<Film> getPopularMoviesByLikes(Integer count);

    Film add(Film film);

    Film update(Film film);

    void addGenres(Film film);

    void removeGenres(Film film);

    /**
     * Метод удаления фильма по ID
     *
     * @param id id фильма
     */
    void removeFilmById(Integer id);
}