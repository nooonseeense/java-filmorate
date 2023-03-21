package ru.application.filmorate.storage.genre;

import ru.application.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    /**
     * Метод получения всех жанров
     *
     * @return Список жанров
     */
    List<Genre> get();

    /**
     * Метод получения названия жанра по id
     *
     * @param id id жанра
     * @return Объект жанра
     */
    Genre getById(int id);
}
