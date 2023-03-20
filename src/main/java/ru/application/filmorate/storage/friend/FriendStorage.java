package ru.application.filmorate.storage.friend;

import ru.application.filmorate.model.User;

import java.util.List;

public interface FriendStorage {

    void addFriends(Integer id, Integer friendId);

    void removeFriends(Integer id, Integer friendId);

    List<User> getListOfFriendsSharedWithAnotherUser(Integer id, Integer otherId);

    List<User> getListOfFriends(Integer id);
}
