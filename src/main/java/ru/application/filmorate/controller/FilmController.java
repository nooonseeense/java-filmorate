package ru.application.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.util.enumeration.FilmSort;
import ru.application.filmorate.service.FilmService;

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
        log.info("Запрос GET: getById() на получение фильма по ID.");
        return filmService.getById(filmId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularMoviesByLikes(@Positive @RequestParam(defaultValue = "10") Integer count,
                                              @RequestParam(required = false) Integer genreId,
                                              @RequestParam(required = false) Short year) {
            return filmService.getPopularMoviesByLikes(count,genreId,year);
    }

    @GetMapping("/search")
    public List<Film> getPopularMoviesFromAdvancedSearch(
                                @RequestParam(value = "query", defaultValue = UNKNOWN) String query,
                                @RequestParam(value = "by", defaultValue = UNKNOWN) String by) {
        return filmService.getPopularMoviesFromAdvancedSearch(query, by);
    }

    @GetMapping("/common")
    public List<Film> getCommonMovies(@RequestParam Integer userId, @RequestParam Integer friendId) {
        log.info("Запрос GET: getCommonMovies() на получение списка общих фильмов юзеров {} и {}.", userId, friendId);
        return filmService.getCommonMovies(userId, friendId);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getBy(@PathVariable int directorId,
                            @RequestParam FilmSort sortBy) {
        log.info("Запрос GET: getBy() на получение списка фильмов режиссера id={}, sortBy={}.", directorId, sortBy);
        return filmService.getBy(directorId, sortBy);
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        log.info("Запрос POST: add() на добавление фильма.");
        return filmService.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Запрос PUT: update() на обновление фильма.");
        return filmService.update(film);
    }

    @PutMapping("{id}/like/{userId}")
    public void addLike(@PositiveOrZero @PathVariable Integer id,
                        @PositiveOrZero @PathVariable Integer userId) {
        log.info("Запрос PUT: addLike() на добавление лайка к фильму {} от юзера {}.", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void removeLike(@PositiveOrZero @PathVariable Integer id,
                           @PositiveOrZero @PathVariable Integer userId) {
        log.info("Запрос DELETE: removeLike() на удаление лайка от юзера {} из фильма {}.", userId, id);
        filmService.removeLike(id, userId);
    }

    @DeleteMapping("{filmId}")
    public void removeFilmById(@PositiveOrZero @PathVariable Integer filmId) {
        log.info("Запрос DELETE: removeFilmById() на удаление фильма {}.", filmId);
        filmService.removeFilmById(filmId);
    }
}