package ru.application.filmorate.impl;

public interface LikeStorage {
    void addLike(int id, int userId);

    void removeLike(int id, int userId);
}
