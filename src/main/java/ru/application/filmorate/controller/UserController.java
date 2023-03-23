package ru.application.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import ru.application.filmorate.model.Event;
import ru.application.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.application.filmorate.model.User;
import ru.application.filmorate.service.EventService;
import ru.application.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EventService eventService;

    @GetMapping
    public List<User> get() {
        log.info("Запрос GET: get() на получение списка всех пользователей.");
        return userService.get();
    }

    @GetMapping("{userId}")
    public User get(@PositiveOrZero @PathVariable Integer userId) {
        log.info("Запрос GET: get(Integer userId) на получение пользователя по ID = {}.", userId);
        return userService.get(userId);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getListOfFriendsSharedWithAnotherUser(@PositiveOrZero @PathVariable Integer id,
                                                            @PositiveOrZero @PathVariable Integer otherId) {
        log.info("Запрос GET: getListOfFriendsSharedWithAnotherUser(Integer id, Integer otherId) на получение списка" +
                " общих друзей по ID = {} первого пользователя и ID = {} второго пользователя.", id, otherId);
        return userService.getListOfFriendsSharedWithAnotherUser(id, otherId);
    }

    @GetMapping("{id}/friends")
    public List<User> getListOfFriends(@PathVariable Integer id) {
        log.info("Запрос GET: getListOfFriends(Integer id) на получение списка друзей по ID = {} пользователя.", id);
        return userService.getListOfFriends(id);
    }

    @GetMapping("{id}/recommendations")
    public List<Film> getRecommendations(@PathVariable Integer id) {
        log.info("Запрос GET: getRecommendations(Integer id) на получение списка рекомендаций для пользователя " +
                "с id = {}", id);
        return userService.getRecommendations(id);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Запрос POST: create(User user) на создание пользователя.");
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Запрос PUT: update(User user) на изменение пользователя.");
        return userService.update(user);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addFriends(@PositiveOrZero @PathVariable Integer id,
                           @PositiveOrZero @PathVariable Integer friendId) {
        log.info("Запрос PUT: addFriends(Integer id, Integer friendId) на добавление пользователю с ID = {} друга" +
                " с ID = {}.", id, friendId);
        userService.addFriends(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void removeFriends(@PositiveOrZero @PathVariable Integer id,
                              @PositiveOrZero @PathVariable Integer friendId) {
        log.info("Запрос DELETE: removeFriends(Integer id, Integer friendId) на удаление пользователю с ID = {} друга " +
                "с ID = {}.", id, friendId);
        userService.removeFriends(id, friendId);
    }

    @DeleteMapping("{userId}")
    public void remove(@PositiveOrZero @PathVariable Integer userId) {
        log.info("Запрос DELETE: remove(Integer userId) на удаление пользователя с ID = {}.", userId);
        userService.remove(userId);
    }

    @GetMapping("{id}/feed")
    public List<Event> getUserEvent(@PathVariable Integer id) {
        log.info("Запрос GET: getUserEvent(Integer id) на получение спаска событий по ID = {} пользователя.", id);
        return eventService.get(id);
    }
}