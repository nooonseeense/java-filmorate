package ru.application.filmorate.storage;

import ru.application.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    void addAsFriend();

    void removeFromFriends();

    List<User> listOfCommonFriends();
}
