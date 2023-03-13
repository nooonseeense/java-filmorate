package ru.application.filmorate.storage;

import ru.application.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    List<Genre> get();

    Genre getById(int id);
}
