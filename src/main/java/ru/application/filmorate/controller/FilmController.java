package ru.application.filmorate.controller;

import lombok.RequiredArgsConstructor;
import ru.application.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.application.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film filmFromRequest) {
        return filmService.getFilmStorage().addFilm(filmFromRequest);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film filmFromRequest) {
        return filmService.getFilmStorage().updateFilm(filmFromRequest);
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getFilmStorage().getFilms();
    }
}
