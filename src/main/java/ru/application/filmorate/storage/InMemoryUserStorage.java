package ru.application.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.exception.UserValidationException;
import ru.application.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 1;

    @Override
    public List<User> get() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return List.copyOf(users.values());
    }

    @Override
    public User getById(Integer userId) {
        checkUserInUsers(userId);
        return users.get(userId);
    }

    @Override
    public List<User> getListOfFriendsSharedWithAnotherUser(Integer id, Integer otherId) {
        checkUserInUsers(id);
        checkUserInUsers(otherId);
        Set<Integer> otherUserFriends = new HashSet<>(users.get(otherId).getFriendsId());
        otherUserFriends.retainAll(users.get(id).getFriendsId());
        return otherUserFriends.stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getListOfFriends(Integer id) {
        checkUserInUsers(id);
        return users.get(id).getFriendsId().stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public User create(User user) {
        if (users.containsValue(user)) {
            String exceptionMessage = "Пользователь уже зарегестрирован.";
            log.warn("Ошибка при добавлении пользователя. Текст исключения: {}", exceptionMessage);
            throw new UserValidationException(exceptionMessage);
        }
        user.setId(generatorId());
        users.put(user.getId(), user);
        log.info("Пользователь создан.");
        return user;
    }

    @Override
    public User update(User user) {
        checkUserInUsers(user.getId());
        users.put(user.getId(), user);
        log.info("Пользователь обновлен.");
        return user;
    }

    @Override
    public User removeFriends(Integer id, Integer friendId) {
        checkUserInUsers(id);
        User user = users.get(id);
        if (!user.getFriendsId().contains(friendId)) {
            String exceptionMessage = "Такого пользователя нет в списке друзей.";
            log.warn("Текст исключения: {}", exceptionMessage);
            throw new UserValidationException(exceptionMessage);
        }
        user.getFriendsId().remove(friendId);
        return user;
    }

    @Override
    public User addFriends(Integer id, Integer friendId) {
        checkUserInUsers(id);
        checkUserInUsers(friendId);
        User user = users.get(id);
        User userFriend = users.get(friendId);
        user.getFriendsId().add(friendId);
        userFriend.getFriendsId().add(id);
        return user;
    }

    private void checkUserInUsers(Integer id) {
        if (!users.containsKey(id)) {
            String exceptionMessage = "Такого пользователя нет в списке.";
            log.warn("Текст исключения: {}", exceptionMessage);
            throw new ObjectWasNotFoundException(exceptionMessage);
        }
    }

    private int generatorId() {
        return userId++;
    }
}
