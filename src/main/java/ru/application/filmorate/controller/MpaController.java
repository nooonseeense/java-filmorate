package ru.application.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.application.filmorate.model.Mpa;
import ru.application.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public List<Mpa> get() {
        log.info("Запрос GET: get() на получение списка всех MPA.");
        return mpaService.get();
    }

    @GetMapping("/{id}")
    public Mpa get(@PathVariable Integer id) {
        log.info("Запрос GET: get(Integer id) на получение MPA по ID = {}.", id);
        return mpaService.get(id);
    }
}
