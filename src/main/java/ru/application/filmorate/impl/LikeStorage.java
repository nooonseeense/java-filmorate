package ru.application.filmorate.impl;

public interface LikeStorage {
    void addLike(int id, int userId);

    void removeLike(int id, int userId);

    /**
     * Метод удаления пользователя по ID из таблицы LIKE_FILM
     *
     * @param id id пользователя
     */
    void removeUserAndLikes(Integer id);
}
