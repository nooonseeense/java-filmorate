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

    public User createUser(User userFromRequest) {
        if (users.containsValue(userFromRequest)) {
            String exceptionMessage = "Пользователь уже зарегестрирован.";
            log.warn("Ошибка при добавлении пользователя. Текст исключения: {}", exceptionMessage);
            throw new UserValidationException(exceptionMessage);
        }
        User user = validationUser(userFromRequest);
        user.setId(generatorId());
        users.put(user.getId(), user);
        log.info("Пользователь создан.");
        return user;
    }

    public List<User> getUsers() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return List.copyOf(users.values());
    }

    public List<User> listOfFriends(Integer id) {
        checkUserInUsers(id);
        return users.get(id).getFriendsId().stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    public List<User> listOfFriendsSharedWithAnotherUser(Integer id, Integer otherId) {
        checkUserInUsers(id);
        checkUserInUsers(otherId);
        Set<Integer> otherUserFriends = new HashSet<>(users.get(otherId).getFriendsId());
        otherUserFriends.retainAll(users.get(id).getFriendsId());
        return otherUserFriends.stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    public User updateUser(User userFromRequest) {
        checkUserInUsers(userFromRequest.getId());
        User user = validationUser(userFromRequest);
        users.remove(userFromRequest.getId());
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
    public List<User> allFriendsUser() {
        return null;
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

    private int generatorId() {
        return userId++;
    }

    private User validationUser(User user) {
        if (user.getLogin().matches(".*\\s+.*")) {
            String exceptionMessage = "Логин не может содержать пробелы.";
            log.warn("Ошибка при валидации пользователя. Текст исключения: {}", exceptionMessage);
            throw new UserValidationException(exceptionMessage);
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }

    public Optional<User> getUserById(Integer userId) {
        checkUserInUsers(userId);
        return Optional.ofNullable(users.get(userId));
    }

    private void checkUserInUsers(Integer id) {
        if (!users.containsKey(id)) {
            String exceptionMessage = "Такого пользователя нет в списке.";
            log.warn("Текст исключения: {}", exceptionMessage);
            throw new ObjectWasNotFoundException(exceptionMessage);
        }
    }
}
