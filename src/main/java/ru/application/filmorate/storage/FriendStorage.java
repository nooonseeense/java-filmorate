package ru.application.filmorate.storage;

import ru.application.filmorate.model.User;

public interface FriendStorage {

    User addFriends(Integer id, Integer friendId);

    User removeFriends(Integer id, Integer friendId);
}
