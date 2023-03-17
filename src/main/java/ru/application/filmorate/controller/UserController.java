package ru.application.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import ru.application.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.application.filmorate.model.Feed;
import ru.application.filmorate.model.User;
import ru.application.filmorate.service.FeedService;
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
    private final FeedService feedService;

    @GetMapping
    public List<User> get() {
        return userService.get();
    }

    @GetMapping("{userId}")
    public User getById(@PositiveOrZero @PathVariable Integer userId) {
        return userService.getById(userId);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getListOfFriendsSharedWithAnotherUser(@PositiveOrZero @PathVariable Integer id,
                                                            @PositiveOrZero @PathVariable Integer otherId) {
        return userService.getListOfFriendsSharedWithAnotherUser(id, otherId);
    }

    @GetMapping("{id}/friends")
    public List<User> getListOfFriends(@PathVariable Integer id) {
        return userService.getListOfFriends(id);
    }

    @GetMapping("{id}/recommendations")
    public List<Film> getRecommendations(@PathVariable Integer id) {
        log.info("Получен список рекомендаций для пользователя с id = {}", id);
        return userService.getRecommendations(id);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addFriends(@PositiveOrZero @PathVariable Integer id,
                           @PositiveOrZero @PathVariable Integer friendId) {
        userService.addFriends(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void removeFriends(@PositiveOrZero @PathVariable Integer id,
                              @PositiveOrZero @PathVariable Integer friendId) {
        userService.removeFriends(id, friendId);
    }

    @DeleteMapping("{userId}")
    public void removeUserById(@PositiveOrZero @PathVariable Integer userId) {
        userService.removeUserById(userId);
    }

    @GetMapping("{id}/feed")
    public List<Feed> getUserFeed(@PathVariable Integer id) {
        return feedService.getFeedByUserId(id);
    }
}