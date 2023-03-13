package ru.application.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.storage.*;

import java.util.LinkedHashSet;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final MpaStorage mpaStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage,
                       LikeStorage likeStorage,
                       FilmGenreStorage filmGenreStorage,
                       MpaStorage mpaStorage) {
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
        this.filmGenreStorage = filmGenreStorage;
        this.mpaStorage = mpaStorage;
    }

    public List<Film> get() {
        return getFilms(filmStorage.get());
    }

    public Film getById(Integer filmId) {
        try {
            Film film = filmStorage.getById(filmId);
            assert film != null;
            film.setGenres(new LinkedHashSet<>(filmGenreStorage.get(filmId)));
            film.setMpa(mpaStorage.getById(film.getMpa().getId()));
            return film;
        } catch (EmptyResultDataAccessException e) {
            String message = String.format("Фильм с идентификатором %d не найден.", filmId);
            throw new ObjectWasNotFoundException(message);
        }
    }

    public List<Film> getPopularMoviesByLikes(Integer count) {
        return getFilms(filmStorage.getPopularMoviesByLikes(count));
    }

    public Film add(Film film) {
        filmStorage.add(film);
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return film;
        }
        return filmGenreStorage.insert(film);
    }

    public Film update(Film film) {
        filmStorage.update(film);
        filmGenreStorage.removeById(film.getId());
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return film;
        }
        return filmGenreStorage.insert(film);
    }

    public void addLike(Integer id, Integer userId) {
        likeStorage.addLike(id, userId);
    }

    public void removeLike(Integer id, Integer userId) {
        likeStorage.removeLike(id, userId);
    }

    private List<Film> getFilms(String sql) {
        List<Film> films = filmStorage.getFilms(sql);
        films.forEach((film) -> {
            film.setGenres(new LinkedHashSet<>(filmGenreStorage.get(film.getId())));
            film.setMpa(mpaStorage.getById(film.getMpa().getId()));
        });
        return films;
    }
}