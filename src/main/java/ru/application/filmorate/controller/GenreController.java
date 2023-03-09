package ru.application.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.application.filmorate.model.Genre;
import ru.application.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public List<Genre> get() {
        return genreService.get();
    }

    @GetMapping("/{id}")
    public Genre getById(@PathVariable int id) {
        return genreService.getById(id);
    }
}
