package ru.application.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.application.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.application.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
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
    public List<Film> getPopularMoviesByLikes(@Positive @RequestParam(defaultValue = "10") Integer count) {
        return filmService.getPopularMoviesByLikes(count);
    }

    @GetMapping("/common")
    public List<Film> getCommonMovies(@RequestParam Integer userId, @RequestParam Integer friendId) {
        return filmService.getCommonMovies(userId, friendId);
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
}