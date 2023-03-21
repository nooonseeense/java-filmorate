package ru.application.filmorate.storage.user;

import ru.application.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> get();
    User getById(Integer userId);
    User create(User user);
    User update(User user);
    void removeUserById(Integer id);
}