package ru.application.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.application.filmorate.model.User;
import ru.application.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> get() {
        return userStorage.get();
    }

    public User getById(Integer userId) {
        return userStorage.getById(userId);
    }

    public List<User> getListOfFriendsSharedWithAnotherUser(Integer id, Integer otherId) {
        return userStorage.getListOfFriendsSharedWithAnotherUser(id, otherId);
    }

    public List<User> getListOfFriends(Integer id) {
        return userStorage.getListOfFriends(id);
    }

    public User create(User user) {
        validation(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        validation(user);
        return userStorage.update(user);
    }

    public User addFriends(Integer id, Integer friendId) {
        return userStorage.addFriends(id, friendId);
    }

    public User removeFriends(Integer id, Integer friendId) {
        return userStorage.removeFriends(id, friendId);
    }

    private void validation(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
