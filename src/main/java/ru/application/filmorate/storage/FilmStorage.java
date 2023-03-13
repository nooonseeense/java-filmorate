package ru.application.filmorate.storage;

import ru.application.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    String get();

    Film getById(Integer filmId);

    String getPopularMoviesByLikes(Integer count);

    Film add(Film film);

    void update(Film film);

    List<Film> getFilms(String sql);
}