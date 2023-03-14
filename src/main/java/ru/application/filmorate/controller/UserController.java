package ru.application.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.application.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.application.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

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
    public void deleteUserById(@PositiveOrZero @PathVariable Integer userId) {
        //userService.getById(userId);
    }
}