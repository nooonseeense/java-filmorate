package ru.application.filmorate.storage;

import ru.application.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> getAllFilms();

    Film updateFilm(Film film);

    Film addFilm(Film film);

    Film addLike(Integer id, Integer userId);

    Film removeLike(Integer id, Integer userId);

    List<Film> listTheTenMostPopularMoviesByTheNumberOfLikes(Integer count);

    Optional<Film> getFilmById(Integer filmId);
}
