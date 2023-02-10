package ru.application.filmorate.storage;

import ru.application.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User userFromRequest);

    User updateUser(User userFromRequest);

    List<User> getUsers();
}
