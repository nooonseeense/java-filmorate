package ru.application.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.application.filmorate.model.Director;
import ru.application.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
@Slf4j
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public List<Director> get() {
        log.info("Запрос GET: get() на получение списка всех режиссеров.");
        return directorService.get();
    }

    @GetMapping("/{id}")
    public Director get(@PathVariable int id) {
        log.info("Запрос GET: get(Integer id) на получение режиссера по id.");
        return directorService.get(id);
    }

    @PostMapping
    public Director create(@Valid @RequestBody Director director) {
        log.info("Запрос POST: create() на создание режиссера.");
        return directorService.create(director);
    }

    @PutMapping
    public Director update(@Valid @RequestBody Director director) {
        log.info("Запрос PUT: update() на изменение режиссера.");
        return directorService.update(director);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        log.info("Запрос DELETE: delete(Integer id) на удаление режиссера.");
        directorService.delete(id);
    }
}
