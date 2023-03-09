package ru.application.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.application.filmorate.model.Mpa;
import ru.application.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public List<Mpa> get() {
        return mpaService.get();
    }

    @GetMapping("/{id}")
    public Mpa getById(@PathVariable int id) {
        return mpaService.getById(id);
    }
}
