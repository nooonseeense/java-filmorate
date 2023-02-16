package ru.application.filmorate.storage;

import ru.application.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> get();

    Film getById(Integer filmId);

    List<Film> getPopularMoviesByLikes(Integer count);

    Film add(Film film);

    Film update(Film film);

    Film addLike(Integer id, Integer userId);

    Film removeLike(Integer id, Integer userId);
}