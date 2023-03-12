package ru.application.filmorate.storage;

import ru.application.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> get();

    User getById(Integer userId);

    List<User> getListOfFriendsSharedWithAnotherUser(Integer id, Integer otherId);

    List<User> getListOfFriends(Integer id);

    User create(User user);

    User update(User user);
}