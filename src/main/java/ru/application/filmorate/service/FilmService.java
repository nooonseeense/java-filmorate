package ru.application.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Lazy FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Optional<Film> getFilmById(Integer filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addLike(Integer id, Integer userId) {
        return filmStorage.addLike(id, userId);
    }

    public Film removeLike(Integer id, Integer userId) {
        return filmStorage.removeLike(id, userId);
    }

    public List<Film> listTheTenMostPopularMoviesByTheNumberOfLikes(Integer count) {
        return filmStorage.listTheTenMostPopularMoviesByTheNumberOfLikes(count);
    }
}
