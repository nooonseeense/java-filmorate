package ru.application.filmorate.storage;

import ru.application.filmorate.model.Film;
import ru.application.filmorate.model.Genre;

import java.util.List;

public interface FilmGenreStorage {

    List<Genre> get(int id);

    Film insert(Film film);

    void removeById(int id);
}
