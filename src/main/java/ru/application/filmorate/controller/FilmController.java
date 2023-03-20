package ru.application.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.model.enums.FilmSort;
import ru.application.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> get() {
        return filmService.get();
    }

    @GetMapping("{filmId}")
    public Film getById(@PositiveOrZero @PathVariable Integer filmId) {
        return filmService.getById(filmId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularMoviesByLikes(@Positive @RequestParam(defaultValue = "10") Integer count,
                                              @RequestParam(required = false) Integer genreId,
                                              @RequestParam(required = false) Short year) {
            return filmService.getPopularMoviesByLikes(count,genreId,year);
    }

    @GetMapping("/common")
    public List<Film> getCommonMovies(@RequestParam Integer userId, @RequestParam Integer friendId) {
        return filmService.getCommonMovies(userId, friendId);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getBy(@PathVariable int directorId,
                            @RequestParam FilmSort sortBy) {
        log.info("Получен запрос на получение списка фильмов режиссера id={}, sortBy={}.", directorId, sortBy);
        return filmService.getBy(directorId, sortBy);
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        return filmService.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @PutMapping("{id}/like/{userId}")
    public void addLike(@PositiveOrZero @PathVariable Integer id,
                        @PositiveOrZero @PathVariable Integer userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void removeLike(@PositiveOrZero @PathVariable Integer id,
                           @PositiveOrZero @PathVariable Integer userId) {
        filmService.removeLike(id, userId);
    }

    @DeleteMapping("{filmId}")
    public void removeFilmById(@PositiveOrZero @PathVariable Integer filmId) {
        filmService.removeFilmById(filmId);
    }
}