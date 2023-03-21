package ru.application.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.service.FilmService;
import ru.application.filmorate.util.enumeration.FilmSort;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.application.filmorate.util.Constants.UNKNOWN;

@Validated
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> get() {
        log.info("Запрос GET: get() на получение списка всех фильмов.");
        return filmService.get();
    }

    @GetMapping("{filmId}")
    public Film getById(@PositiveOrZero @PathVariable Integer filmId) {
        log.info("Запрос GET: getById(Integer filmId) на получение фильма по ID = {}.", filmId);
        return filmService.getById(filmId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularMoviesByLikes(@Positive @RequestParam(defaultValue = "10") Integer count,
                                              @RequestParam(required = false) Integer genreId,
                                              @RequestParam(required = false) Short year) {
        log.info("Запрос GET: getPopularMoviesByLikes(Integer count, Integer genreId, Short year) на получение списка " +
                "популярных фильмов с параметрами: COUNT = {}, GENRE ID = {}, YEAR = {}.", count, genreId, year);
        return filmService.getPopularMoviesByLikes(count, genreId, year);
    }

    @GetMapping("/search")
    public List<Film> getPopularMoviesFromAdvancedSearch(
            @RequestParam(value = "query", defaultValue = UNKNOWN) String query,
            @RequestParam(value = "by", defaultValue = UNKNOWN) String by) {
        log.info("Запрос GET: getPopularMoviesFromAdvancedSearch(String query, String by) на получение списка " +
                "популярных фильмов расширенного поиска параметрами: QUERY = {}, BY = {}.", query, by);
        return filmService.getPopularMoviesFromAdvancedSearch(query, by);
    }

    @GetMapping("/common")
    public List<Film> getCommonMovies(@RequestParam Integer userId, @RequestParam Integer friendId) {
        log.info("Запрос GET: getCommonMovies(Integer userId, Integer friendId) на получение списка общих фильмов " +
                "пользователей {} и {}.", userId, friendId);
        return filmService.getCommonMovies(userId, friendId);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getBy(@PathVariable Integer directorId,
                            @RequestParam FilmSort sortBy) {
        log.info("Запрос GET: getBy(Integer directorId, FilmSort sortBy) на получение списка фильмов " +
                "режиссера ID = {}, SORT BY = {}.", directorId, sortBy);
        return filmService.getBy(directorId, sortBy);
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        log.info("Запрос POST: add(Film film) на добавление фильма.");
        return filmService.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Запрос PUT: update(Film film) на обновление фильма.");
        return filmService.update(film);
    }

    @PutMapping("{id}/like/{userId}")
    public void addLike(@PositiveOrZero @PathVariable Integer id,
                        @PositiveOrZero @PathVariable Integer userId) {
        log.info("Запрос PUT: addLike(Integer id, Integer userId) на добавление лайка к фильму {} от юзера {}.",
                id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void removeLike(@PositiveOrZero @PathVariable Integer id,
                           @PositiveOrZero @PathVariable Integer userId) {
        log.info("Запрос DELETE: removeLike(Integer id, Integer userId) на удаление лайка от юзера {} из фильма {}.",
                userId, id);
        filmService.removeLike(id, userId);
    }

    @DeleteMapping("{filmId}")
    public void removeFilm(@PositiveOrZero @PathVariable Integer filmId) {
        log.info("Запрос DELETE: removeFilm(Integer filmId) на удаление фильма {}.", filmId);
        filmService.removeFilmById(filmId);
    }
}