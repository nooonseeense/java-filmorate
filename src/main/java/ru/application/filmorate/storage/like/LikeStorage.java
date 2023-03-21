package ru.application.filmorate.storage.like;

public interface LikeStorage {
    void addLike(int id, int userId);

    void removeLike(int id, int userId);
}
