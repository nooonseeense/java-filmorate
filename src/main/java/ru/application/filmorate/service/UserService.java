package ru.application.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.application.filmorate.enums.EventType;
import ru.application.filmorate.enums.Operation;
import ru.application.filmorate.impl.FilmStorage;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.model.User;
import ru.application.filmorate.impl.FriendStorage;
import ru.application.filmorate.impl.UserStorage;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;
    private final FilmStorage filmStorage;
    private final FeedService feedService;

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
        userStorage.getById(id);
        return friendStorage.getListOfFriends(id);
    }

    public List<Film> getRecommendations(Integer userId) {
        log.debug("Получение рекомендаций для пользователя с ID {}", userId);
        validation(userStorage.getById(userId));
        List<Integer> matchingUserIds = new ArrayList<>(userStorage.getMatchingUserIds(userId));

        if (matchingUserIds.isEmpty()) {
            return new ArrayList<>();
        }

        return filmStorage.getRecommendedFilms(userId, matchingUserIds);
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
        feedService.createFeed(id, EventType.FRIEND, Operation.ADD, friendId);
    }

    public void removeFriends(Integer id, Integer friendId) {
        friendStorage.removeFriends(id, friendId);
        feedService.createFeed(id, EventType.FRIEND, Operation.REMOVE, friendId);
    }

    private void validation(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    /**
     * Метод удаления пользователя по ID
     *
     * @param id id пользователя
     */
    public void removeUserById(Integer id) {
        log.debug("Получен запрос на удаление пользователя по id = {}", id);
        userStorage.removeUserById(id);
    }
}


