package ru.application.filmorate.storage;

import org.springframework.web.bind.annotation.PathVariable;
import ru.application.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User createUser(User user);

    List<User> getUsers();

    List<User> listOfFriends(@PathVariable Integer id);

    List<User> listOfFriendsSharedWithAnotherUser(Integer id, Integer otherId);

    User updateUser(User user);

    User removeFriends(Integer id, Integer friendId);

    List<User> allFriendsUser();

    User addFriends(Integer id, Integer friendId);

    Optional<User> getUserById(Integer userId);
}
