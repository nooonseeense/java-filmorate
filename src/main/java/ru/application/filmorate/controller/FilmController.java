package ru.application.filmorate.controller;

import ru.application.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.application.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/films")
@RestController
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

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
        return filmService.getFilms();
    }
}
