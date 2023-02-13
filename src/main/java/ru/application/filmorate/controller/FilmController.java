package ru.application.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import ru.application.filmorate.exception.FilmValidationException;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.application.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film filmFromRequest) {
        return filmService.addFilm(filmFromRequest);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film filmFromRequest) {
        return filmService.updateFilm(filmFromRequest);
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("{filmId}")
    public Optional<Film> getFilmById(@PositiveOrZero @PathVariable Integer filmId) {
        return filmService.getFilmById(filmId);
    }

    @GetMapping("/popular")
    public List<Film> listTheTenMostPopularMoviesByTheNumberOfLikes(
            @PositiveOrZero @RequestParam(defaultValue = "10") Integer count) {
        return filmService.listTheTenMostPopularMoviesByTheNumberOfLikes(count);
    }

    @PutMapping("{id}/like/{userId}")
    public Film addLike(@PositiveOrZero @PathVariable Integer id,
                        @PositiveOrZero @PathVariable Integer userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Film removeLike(@PositiveOrZero @PathVariable Integer id,
                           @PositiveOrZero @PathVariable Integer userId) {
        return filmService.removeLike(id, userId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleFilmValidateEx(final FilmValidationException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleFilmNotFoundEx(final ObjectWasNotFoundException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleNullOrIllegalArgumentExc(final RuntimeException e) {
        return Map.of("error", e.getMessage());
    }
}
