package ru.application.filmorate.storage.director;

import ru.application.filmorate.model.Director;
import ru.application.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface DirectorStorage {
    /**
     * Метод получения списка режиссёров
     *
     * @return Список режиссёров
     */
    List<Director> get();

    /**
     * Метод получения объекта режиссёра
     *
     * @param id id режиссёра
     * @return Объект режиссёра
     */
    Optional<Director> get(Integer id);

    /**
     * Метод создания режиссёра
     *
     * @param director Объект режиссёра
     * @return Объект режиссёра
     */
    Optional<Director> create(Director director);

    /**
     * Метод изменения объекта режиссёра
     *
     * @param director Объект режиссёра
     * @return Изменённый объект режиссёра
     */
    Director update(Director director);

    /**
     * Метод удаления режиссёра по ID
     *
     * @param id id режиссёра
     */
    void delete(int id);

    /**
     * Метод добавления режиссёра в фильм
     *
     * @param films Список фильмов
     */
    void setDirectors(List<Film> films);
}
