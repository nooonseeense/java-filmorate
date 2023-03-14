package ru.application.filmorate.impl;

import ru.application.filmorate.model.Film;
import ru.application.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    List<Mpa> get();
    Mpa getById(int id);
    void setMpa(Film film);
}
