package ru.application.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.application.filmorate.model.User;
import ru.application.filmorate.dao.UserDbStorageDao;

import java.util.List;

@Service
public class UserService {
    private final UserDbStorageDao userDbStorageDao;

    @Autowired
    public UserService(UserDbStorageDao userDbStorageDao) {
        this.userDbStorageDao = userDbStorageDao;
    }

    public List<User> get() {
        return userDbStorageDao.get();
    }

    public User getById(Integer userId) {
        return userDbStorageDao.getById(userId);
    }

    public List<User> getListOfFriendsSharedWithAnotherUser(Integer id, Integer otherId) {
        return userDbStorageDao.getListOfFriendsSharedWithAnotherUser(id, otherId);
    }

    public List<User> getListOfFriends(Integer id) {
        return userDbStorageDao.getListOfFriends(id);
    }

    public User create(User user) {
        validation(user);
        return userDbStorageDao.create(user);
    }

    public User update(User user) {
        validation(user);
        return userDbStorageDao.update(user);
    }

    public User addFriends(Integer id, Integer friendId) {
        return userDbStorageDao.addFriends(id, friendId);
    }

    public User removeFriends(Integer id, Integer friendId) {
        return userDbStorageDao.removeFriends(id, friendId);
    }

    private void validation(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
