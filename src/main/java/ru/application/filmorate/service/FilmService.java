package ru.application.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.storage.FilmStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("InDbFilmStorage") @Lazy FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> get() {
        return filmStorage.get();
    }

    public Film getById(Integer filmId) {
        return filmStorage.getById(filmId);
    }

    public List<Film> getPopularMoviesByLikes(Integer count) {
        return filmStorage.getPopularMoviesByLikes(count);
    }

    public Film add(Film film) {
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film addLike(Integer id, Integer userId) {
        return filmStorage.addLike(id, userId);
    }

    public Film removeLike(Integer id, Integer userId) {
        return filmStorage.removeLike(id, userId);
    }
}