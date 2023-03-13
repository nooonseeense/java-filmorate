package ru.application.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.application.filmorate.model.User;
import ru.application.filmorate.storage.FriendStorage;
import ru.application.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @Autowired
    public UserService(UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public List<User> get() {
        return userStorage.get();
    }

    public User getById(Integer userId) {
        return userStorage.getById(userId);
    }

    public List<User> getListOfFriendsSharedWithAnotherUser(Integer id, Integer otherId) {
        return friendStorage.getListOfFriendsSharedWithAnotherUser(id, otherId);
    }

    public List<User> getListOfFriends(Integer id) {
        return friendStorage.getListOfFriends(id);
    }

    public User create(User user) {
        validation(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        validation(user);
        return userStorage.update(user);
    }

    public void addFriends(Integer id, Integer friendId) {
        friendStorage.addFriends(id, friendId);
    }

    public void removeFriends(Integer id, Integer friendId) {
        friendStorage.removeFriends(id, friendId);
    }

    private void validation(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
