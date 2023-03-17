package ru.application.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.application.filmorate.exception.IncorrectParameterException;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.impl.FilmGenreStorage;
import ru.application.filmorate.impl.FilmStorage;
import ru.application.filmorate.impl.LikeStorage;
import ru.application.filmorate.impl.MpaStorage;
import ru.application.filmorate.model.Film;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import static ru.application.filmorate.util.Constants.SAMPLE;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final MpaStorage mpaStorage;

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

    /**
     * Метод расширенного поиска списка фильмов по определённым параметрам
     *
     * @param query текст для поиска
     * @param by    может принимать значения director (поиск по режиссёру), title (поиск по названию)
     * @return Список фильмов
     */
    public List<Film> getPopularMoviesFromAdvancedSearch(String query, String by) {
        List<Film> resultPopularMoviesFromAdvancedSearch;
        if (!SAMPLE.contains(by)) {
            throw new IncorrectParameterException("Некорректное значение выборки поиска");
        }
        log.debug("Получен запрос на расширенный поиск текста = {}, по табл. = {}", query, by);
        resultPopularMoviesFromAdvancedSearch = filmStorage.getPopularMoviesFromAdvancedSearch(query, by);
        filmGenreStorage.setGenres(resultPopularMoviesFromAdvancedSearch);
        return resultPopularMoviesFromAdvancedSearch;
    }

    public List<Film> getCommonMovies(Integer userId, Integer friendId) {
        List<Film> commonMovies = filmStorage.getCommonMovies(userId, friendId);
        filmGenreStorage.setGenres(commonMovies);
        return commonMovies;
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
    }

    public void removeLike(Integer id, Integer userId) {
        likeStorage.removeLike(id, userId);
    }
}