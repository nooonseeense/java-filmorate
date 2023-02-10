package ru.application.filmorate.storage;

import ru.application.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getFilms();

    Film updateFilm(Film filmFromRequest);

    Film addFilm(Film filmFromRequest);
}
