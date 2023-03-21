package ru.application.filmorate.storage.like;

public interface LikeStorage {
    /**
     * Метод добавления Like фильму
     *
     * @param id     id фильма
     * @param userId id пользователя
     */
    void addLike(int id, int userId);

    /**
     * Метод удаления Like у фильма
     *
     * @param id     id фильма
     * @param userId id пользователя
     */
    void removeLike(int id, int userId);
}
