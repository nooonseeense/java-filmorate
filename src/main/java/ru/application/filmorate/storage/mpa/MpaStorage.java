package ru.application.filmorate.storage.mpa;

import ru.application.filmorate.model.Film;
import ru.application.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {

    /**
     * Метод получения всех MPA
     *
     * @return Список MPA
     */
    List<Mpa> get();

    /**
     * Метод получения названия MPA по id
     *
     * @param id id MPA
     * @return объект MPA
     */
    Mpa getById(int id);

    /**
     * Метод добавления MPA по объекту film
     *
     * @param film объект film
     */
    void setMpa(Film film);
}
