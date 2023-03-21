package ru.application.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.application.filmorate.exception.IncorrectParameterException;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.model.Director;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.storage.director.DirectorStorage;
import ru.application.filmorate.storage.film.FilmStorage;
import ru.application.filmorate.storage.filmgenre.FilmGenreStorage;
import ru.application.filmorate.storage.like.LikeStorage;
import ru.application.filmorate.storage.mpa.MpaStorage;
import ru.application.filmorate.util.enumeration.EventType;
import ru.application.filmorate.util.enumeration.FilmSort;
import ru.application.filmorate.util.enumeration.Operation;

import java.util.*;

import static ru.application.filmorate.util.Constants.SAMPLE;

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
        filmGenreStorage.setGenres(films);
        directorStorage.setDirectors(films);
        return films;
    }

    /**
     * Метод получения объекта фильма по ID
     *
     * @param filmId id фильма
     * @return Объект фильма
     */
    public Film getById(Integer filmId) {
        try {
            Film film = filmStorage.getById(filmId);
            assert film != null;
            film.setGenres(new LinkedHashSet<>(filmGenreStorage.get(filmId)));
             directorStorage.setDirectors(Collections.singletonList(film));
            return film;
        } catch (EmptyResultDataAccessException e) {
            String message = String.format("Фильм с id = %d не найден.", filmId);
            log.debug("FilmService getById(Integer filmId): Фильм с id = {} не найден.", filmId);
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
        List<Film> popularMoviesByLikes = new ArrayList<>();
        if (genreId == null && year == null) {
             popularMoviesByLikes = filmStorage.getPopularMoviesByLikes(count);
        }
        if (genreId != null && year != null) {
            popularMoviesByLikes = filmStorage.getPopularMoviesByLikes(count, genreId, year);
        } else if (genreId != null) {
            popularMoviesByLikes = filmStorage.getPopularMoviesByLikes(count, genreId);
        } else if (year != null) {
            popularMoviesByLikes = filmStorage.getPopularMoviesByLikes(count, year);
        }
        filmGenreStorage.setGenres(popularMoviesByLikes);
        directorStorage.setDirectors(popularMoviesByLikes);
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
            log.debug("FilmService getPopularMoviesFromAdvancedSearch(String query, String by): Некорректное значение " +
                    "выборки поиска в поле BY = {}", by);
            throw new IncorrectParameterException("Некорректное значение выборки поиска");
        }
        resultPopularMoviesFromAdvancedSearch = filmStorage.getPopularMoviesFromAdvancedSearch(query, by);
        filmGenreStorage.setGenres(resultPopularMoviesFromAdvancedSearch);
        directorStorage.setDirectors(resultPopularMoviesFromAdvancedSearch);
        return resultPopularMoviesFromAdvancedSearch;
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
        filmGenreStorage.setGenres(commonMovies);
        directorStorage.setDirectors(commonMovies);
        return commonMovies;
    }

    /**
     * Метод получения списка фильмов по ID режиссёра
     *
     * @param directorId id режиссёра
     * @param sortBy     Сортировка по лайкам или году
     * @return Список фильмов по ID режиссёра
     */
    public List<Film> getBy(int directorId, FilmSort sortBy) {
        Optional<Director> director = directorStorage.get(directorId);
        if (director.isEmpty()) {
            String message = String.format("Режиссер с id = %d не найден.", directorId);
            log.debug("FilmService getBy(int directorId, FilmSort sortBy): Режиссер с id = {} не найден.", directorId);
            throw new ObjectWasNotFoundException(message);
        }
        List<Film> films = filmStorage.getBy(directorId, sortBy);
        filmGenreStorage.setGenres(films);
        directorStorage.setDirectors(films);
        return films;
    }

    /**
     * Метод создания фильма
     *
     * @param film Объект фильма
     * @return Созданный объект фильма
     */
    public Film add(Film film) {
        mpaStorage.setMpa(film);
        Film newFilm = filmStorage.add(film);
        filmGenreStorage.setGenres(Collections.singletonList(newFilm));
        directorStorage.setDirectors(Collections.singletonList(newFilm));
        return film;
    }

    /**
     * Метод изменения объекта фильм
     *
     * @param film Объект фильма
     * @return Изменённый объект фильма
     */
    public Film update(Film film) {
        mpaStorage.setMpa(film);
        Film updatedFilm = filmStorage.update(film);
        filmGenreStorage.setGenres(Collections.singletonList(updatedFilm));
        directorStorage.setDirectors(Collections.singletonList(updatedFilm));
        return film;
    }

    /**
     * Метод добавления Like фильму
     *
     * @param id     id фильма
     * @param userId id пользователя
     */
    public void addLike(Integer id, Integer userId) {
        likeStorage.addLike(id, userId);
        eventService.createEvent(userId, EventType.LIKE, Operation.ADD, id);
    }

    /**
     * Метод удаления Like у фильма
     *
     * @param id     id фильма
     * @param userId id пользователя
     */
    public void removeLike(Integer id, Integer userId) {
        likeStorage.removeLike(id, userId);
        eventService.createEvent(userId, EventType.LIKE, Operation.REMOVE, id);
    }

    /**
     * Метод удаления фильма по ID
     *
     * @param id id фильма
     */
    public void removeFilmById(Integer id) {
        filmStorage.removeFilmById(id);
    }
}