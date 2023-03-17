package ru.application.filmorate.impl;

import ru.application.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> get();

    User getById(Integer userId);

    User create(User user);

    User update(User user);

    /**
     * Метод удаления пользователя по ID
     *
     * @param id id пользователя
     */
    void removeUserById(Integer id);
}