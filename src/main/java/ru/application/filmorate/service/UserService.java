package ru.application.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.application.filmorate.model.User;
import ru.application.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public List<User> listOfFriends(Integer id) {
        return userStorage.listOfFriends(id);
    }

    public List<User> listOfFriendsSharedWithAnotherUser(Integer id, Integer otherId) {
        return userStorage.listOfFriendsSharedWithAnotherUser(id, otherId);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User addFriends(Integer id, Integer friendId) {
        return userStorage.addFriends(id, friendId);
    }

    public List<User> allFriendsUser() {
        return userStorage.allFriendsUser();
    }

    public Optional<User> getUserById(Integer userId) {
        return userStorage.getUserById(userId);
    }

    public User removeFriends(Integer id, Integer friendId) {
        return userStorage.removeFriends(id, friendId);
    }
}
