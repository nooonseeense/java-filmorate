package ru.application.filmorate.storage.filmgenre;

import ru.application.filmorate.model.Film;
import ru.application.filmorate.model.Genre;

import java.util.List;

public interface FilmGenreStorage {
    /**
     * Метод получения списка всех жанров
     *
     * @return Список всех жанров
     */
    List<Genre> get(int id);

    /**
     * Метод добавления жанра в список объектов фильмов
     *
     * @param films Список объектов фильмов
     */
    void setGenres(List<Film> films);
}
