package ru.application.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.model.Director;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.impl.*;
import ru.application.filmorate.model.enums.FilmSort;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final DirectorStorage directorStorage;
    private final MpaStorage mpaStorage;

    public List<Film> get() {
        List<Film> films = filmStorage.get();
        filmGenreStorage.setGenres(films);
        directorStorage.setDirectors(films);
        return films;
    }

    public Film getById(Integer filmId) {
        try {
            Film film = filmStorage.getById(filmId);
            assert film != null;
            film.setGenres(new LinkedHashSet<>(filmGenreStorage.get(filmId)));
            directorStorage.setDirectors(Collections.singletonList(film));
            return film;
        } catch (EmptyResultDataAccessException e) {
            String message = String.format("Фильм с id = %d не найден.", filmId);
            log.debug(message);
            throw new ObjectWasNotFoundException(message);
        }
    }

    public List<Film> getPopularMoviesByLikes(Integer count) {
        List<Film> popularMoviesByLikes = filmStorage.getPopularMoviesByLikes(count);
        filmGenreStorage.setGenres(popularMoviesByLikes);
        directorStorage.setDirectors(popularMoviesByLikes);
        return popularMoviesByLikes;
    }

    public List<Film> getCommonMovies(Integer userId, Integer friendId) {
        List<Film> commonMovies = filmStorage.getCommonMovies(userId, friendId);
        filmGenreStorage.setGenres(commonMovies);
        directorStorage.setDirectors(commonMovies);
        return commonMovies;
    }

    public List<Film> getBy(int directorId, FilmSort sortBy) {
        Optional<Director> director = directorStorage.get(directorId);
        if (director.isEmpty()) {
            String message = String.format("Режиссер с id = %d не найден.", directorId);
            log.debug(message);
            throw new ObjectWasNotFoundException(message);
        }
        List<Film> films = filmStorage.getBy(directorId, sortBy);
        filmGenreStorage.setGenres(films);
        directorStorage.setDirectors(films);
        return films;
    }

    public Film add(Film film) {
        mpaStorage.setMpa(film);
        Film newFilm = filmStorage.add(film);
        filmGenreStorage.setGenres(Collections.singletonList(newFilm));
        directorStorage.setDirectors(Collections.singletonList(newFilm));
        return film;
    }

    public Film update(Film film) {
        mpaStorage.setMpa(film);
        Film updatedFilm = filmStorage.update(film);
        filmGenreStorage.setGenres(Collections.singletonList(updatedFilm));
        directorStorage.setDirectors(Collections.singletonList(updatedFilm));
        return film;
    }

    public void addLike(Integer id, Integer userId) {
        likeStorage.addLike(id, userId);
    }

    public void removeLike(Integer id, Integer userId) {
        likeStorage.removeLike(id, userId);
    }
}