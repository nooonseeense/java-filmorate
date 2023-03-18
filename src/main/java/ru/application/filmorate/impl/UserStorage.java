package ru.application.filmorate.impl;

import ru.application.filmorate.model.LikeFilm;
import ru.application.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    List<User> get();

    User getById(Integer userId);

    User create(User user);

    User update(User user);

    List<LikeFilm> getUserLikes(Integer userId);

    Set<Integer> getMatchingUserIds(Integer userId, List<LikeFilm> userLikes);

    /**
     * Метод удаления пользователя по ID
     *
     * @param id id пользователя
     */
    void removeUserById(Integer id);
}