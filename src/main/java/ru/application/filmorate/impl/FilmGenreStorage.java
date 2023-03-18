package ru.application.filmorate.impl;

import ru.application.filmorate.model.Film;
import ru.application.filmorate.model.Genre;

import java.util.List;
import java.util.Map;

public interface FilmGenreStorage {
    List<Genre> get(int id);
    void setGenres(List<Film> films);
}
