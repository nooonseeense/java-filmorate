package ru.application.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.application.filmorate.enums.EventType;
import ru.application.filmorate.enums.Operation;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.impl.FilmGenreStorage;
import ru.application.filmorate.impl.FilmStorage;
import ru.application.filmorate.impl.LikeStorage;
import ru.application.filmorate.impl.MpaStorage;
import ru.application.filmorate.model.Film;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final MpaStorage mpaStorage;
    private final FeedService feedService;

    public List<Film> get() {
        List<Film> films = filmStorage.get();
        filmGenreStorage.setGenres(films);
        return films;
    }

    public Film getById(Integer filmId) {
        try {
            Film film = filmStorage.getById(filmId);
            assert film != null;
            film.setGenres(new LinkedHashSet<>(filmGenreStorage.get(filmId)));
            return film;
        } catch (EmptyResultDataAccessException e) {
            String message = String.format("Фильм с идентификатором %d не найден.", filmId);
            throw new ObjectWasNotFoundException(message);
        }
    }

    public List<Film> getPopularMoviesByLikes(Integer count) {
        List<Film> popularMoviesByLikes = filmStorage.getPopularMoviesByLikes(count);
        filmGenreStorage.setGenres(popularMoviesByLikes);
        return popularMoviesByLikes;
    }

    public Film add(Film film) {
        mpaStorage.setMpa(film);
        Film newFilm = filmStorage.add(film);
        filmGenreStorage.setGenres(Collections.singletonList(newFilm));
        return film;
    }

    public Film update(Film film) {
        mpaStorage.setMpa(film);
        Film updatedFilm = filmStorage.update(film);
        filmGenreStorage.setGenres(Collections.singletonList(updatedFilm));
        return film;
    }

    public void addLike(Integer id, Integer userId) {
        likeStorage.addLike(id, userId);
        feedService.createFeed(userId, EventType.LIKE, Operation.ADD, id);
    }

    public void removeLike(Integer id, Integer userId) {
        likeStorage.removeLike(id, userId);
        feedService.createFeed(userId, EventType.LIKE, Operation.REMOVE, id);
    }

    /**
     * Метод удаления фильма по ID
     *
     * @param id id фильма
     */
    public void removeFilmById(Integer id) {
        log.debug("Получен запрос на удаление пользователя по id = {}", id);
        filmStorage.removeFilmById(id);
    }
}