package ru.application.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.application.filmorate.exception.IncorrectParameterException;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.model.Director;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.storage.*;
import ru.application.filmorate.storage.util.enumeration.EventType;
import ru.application.filmorate.storage.util.enumeration.FilmSortType;
import ru.application.filmorate.storage.util.enumeration.OperationType;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import static ru.application.filmorate.storage.util.constant.Constants.SAMPLE;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final DirectorStorage directorStorage;
    private final MpaStorage mpaStorage;
    private final EventService eventService;

    /**
     * Метод получения списка всех фильмов
     *
     * @return Список всех фильмов
     */
    public List<Film> get() {
        List<Film> films = filmStorage.get();
        filmGenreStorage.set(films);
        directorStorage.set(films);
        return films;
    }

    /**
     * Метод получения объекта фильма по ID
     *
     * @param filmId id фильма
     * @return Объект фильма
     */
    public Film get(Integer filmId) {
        try {
            Film film = filmStorage.get(filmId);
            film.setGenres(new LinkedHashSet<>(filmGenreStorage.get(filmId)));
            directorStorage.set(Collections.singletonList(film));
            return film;
        } catch (EmptyResultDataAccessException e) {
            String message = String.format("Фильм с id = %d не найден.", filmId);
            log.debug("get(Integer filmId): Фильм с id = {} не найден.", filmId);
            throw new ObjectWasNotFoundException(message);
        }
    }

    /**
     * Метод получения списка популярных фильмов по жанру и году
     *
     * @param count   Количество отображаемых фильмов в списке
     * @param genreId id жанра
     * @param year    Запрашиваемый год
     * @return Список фильмов
     */
    public List<Film> getPopularMoviesByLikes(Integer count, Integer genreId, Short year) {
        List<Film> films = filmStorage.getPopularMoviesByLikes(count, genreId, year);
        filmGenreStorage.set(films);
        directorStorage.set(films);
        return films;
    }

    /**
     * Метод расширенного поиска списка фильмов по определённым параметрам
     *
     * @param query текст для поиска
     * @param by    может принимать значения director (поиск по режиссёру), title (поиск по названию)
     * @return Список фильмов
     */
    public List<Film> getPopularMoviesFromAdvancedSearch(String query, String by) {
        if (!SAMPLE.contains(by)) {
            log.debug("getPopularMoviesFromAdvancedSearch(String query, String by): " +
                    "Некорректное значение выборки поиска в поле BY = {}", by);
            throw new IncorrectParameterException("Некорректное значение выборки поиска");
        }
        List<Film> films = filmStorage.getPopularMoviesFromAdvancedSearch(query, by);
        filmGenreStorage.set(films);
        directorStorage.set(films);
        return films;
    }

    /**
     * Метод получения списка общих фильмов
     *
     * @param userId   id первого пользователя
     * @param friendId id второго пользователя
     * @return Список общих фильмов
     */
    public List<Film> getCommonMovies(Integer userId, Integer friendId) {
        List<Film> commonMovies = filmStorage.getCommonMovies(userId, friendId);
        filmGenreStorage.set(commonMovies);
        directorStorage.set(commonMovies);
        return commonMovies;
    }

    /**
     * Метод получения списка фильмов по ID режиссёра
     *
     * @param directorId id режиссёра
     * @param sortBy     Сортировка по лайкам или году
     * @return Список фильмов по ID режиссёра
     */
    public List<Film> get(int directorId, FilmSortType sortBy) {
        Optional<Director> director = directorStorage.get(directorId);
        if (director.isEmpty()) {
            String message = String.format("Режиссер с id = %d не найден.", directorId);
            log.debug("get(int directorId, FilmSort sortBy): Режиссер с id = {} не найден.", directorId);
            throw new ObjectWasNotFoundException(message);
        }
        List<Film> films = filmStorage.get(directorId, sortBy);
        filmGenreStorage.set(films);
        directorStorage.set(films);
        return films;
    }

    /**
     * Метод создания фильма
     *
     * @param film Объект фильма
     * @return Созданный объект фильма
     */
    public Film add(Film film) {
        mpaStorage.set(film);
        Film newFilm = filmStorage.add(film);
        filmGenreStorage.set(Collections.singletonList(newFilm));
        directorStorage.set(Collections.singletonList(newFilm));
        return film;
    }

    /**
     * Метод изменения объекта фильм
     *
     * @param film Объект фильма
     * @return Изменённый объект фильма
     */
    public Film update(Film film) {
        mpaStorage.set(film);
        Film updatedFilm = filmStorage.update(film);
        filmGenreStorage.set(Collections.singletonList(updatedFilm));
        directorStorage.set(Collections.singletonList(updatedFilm));
        return film;
    }

    /**
     * Метод добавления Like фильму
     *
     * @param id     id фильма
     * @param userId id пользователя
     */
    public void addLike(Integer id, Integer userId) {
        likeStorage.add(id, userId);
        eventService.create(userId, EventType.LIKE, OperationType.ADD, id);
    }

    /**
     * Метод удаления Like у фильма
     *
     * @param id     id фильма
     * @param userId id пользователя
     */
    public void removeLike(Integer id, Integer userId) {
        likeStorage.remove(id, userId);
        eventService.create(userId, EventType.LIKE, OperationType.REMOVE, id);
    }

    /**
     * Метод удаления фильма по ID
     *
     * @param id id фильма
     */
    public void remove(Integer id) {
        filmStorage.remove(id);
    }

    /**
     * Метод проверки фильма в БД
     *
     * @param filmId id фильма
     */
    public void exists(Integer filmId) {
        if (!filmStorage.isExist(filmId)) {
            log.debug("Фильм с id: {} не найден", filmId);
            throw new ObjectWasNotFoundException(String.format("Фильм с id: %s не найден", filmId));
        }
    }
}
