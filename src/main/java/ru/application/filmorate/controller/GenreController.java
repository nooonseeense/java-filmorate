package ru.application.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.application.filmorate.model.Genre;
import ru.application.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public List<Genre> get() {
        log.info("Запрос GET: get() на получение списка всех жанров.");
        return genreService.get();
    }

    @GetMapping("/{id}")
    public Genre get(@PathVariable Integer id) {
        log.info("Запрос GET: get(Integer id) на получение жанра по ID = {}.", id);
        return genreService.get(id);
    }
}
