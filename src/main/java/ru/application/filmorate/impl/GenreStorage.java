package ru.application.filmorate.impl;

import ru.application.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    List<Genre> get();

    Genre getById(int id);
}
