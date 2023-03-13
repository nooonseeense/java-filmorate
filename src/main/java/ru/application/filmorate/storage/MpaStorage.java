package ru.application.filmorate.storage;

import ru.application.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    List<Mpa> get();
    Mpa getById(int id);
}
