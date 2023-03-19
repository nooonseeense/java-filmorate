package ru.application.filmorate.impl;

import ru.application.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> get();
    Film getById(Integer filmId);
    List<Film> getPopularMoviesByLikes(Integer count);
    List<Film> getPopularMoviesByLikes(Integer count,Integer genreId);
    List<Film> getPopularMoviesByLikes(Integer count,Short year);
    List<Film> getPopularMoviesByLikes(Integer count,Integer genreId,Short year);
    List<Film> getCommonMovies(Integer userId, Integer friendId);
    Film add(Film film);
    Film update(Film film);
    void addGenres(Film film);
    void removeGenres(Film film);
    List<Film> getRecommendedFilms(Integer userId);
    void removeFilmById(Integer id);
}