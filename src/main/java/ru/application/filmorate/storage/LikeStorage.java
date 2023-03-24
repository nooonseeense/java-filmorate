package ru.application.filmorate.storage;

public interface LikeStorage {
    /**
     * Метод добавления Like фильму
     *
     * @param id     id фильма
     * @param userId id пользователя
     */
    void add(int id, int userId);

    /**
     * Метод удаления Like у фильма
     *
     * @param id     id фильма
     * @param userId id пользователя
     */
    void remove(int id, int userId);
}
