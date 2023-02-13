package ru.application.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.exception.UserValidationException;
import ru.application.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.application.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("{id}/friends")
    public List<User> listOfFriends(@PathVariable Integer id) {
        return userService.listOfFriends(id);
    }

    @GetMapping("{userId}")
    public Optional<User> getUserById(@PositiveOrZero @PathVariable Integer userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> listOfFriendsSharedWithAnotherUser(@PositiveOrZero @PathVariable Integer id,
                                                         @PositiveOrZero @PathVariable Integer otherId) {
        return userService.listOfFriendsSharedWithAnotherUser(id, otherId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("{id}/friends/{friendId}")
    public User addFriends(@PositiveOrZero @PathVariable Integer id,
                           @PositiveOrZero @PathVariable Integer friendId) {
        return userService.addFriends(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public User removeFriends(@PositiveOrZero @PathVariable Integer id,
                              @PositiveOrZero @PathVariable Integer friendId) {
        return userService.removeFriends(id, friendId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUserValidateEx(final UserValidationException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleUserNotFoundEx(final ObjectWasNotFoundException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleNullOrIllegalArgumentEx(final RuntimeException e) {
        return Map.of("error", e.getMessage());
    }
}