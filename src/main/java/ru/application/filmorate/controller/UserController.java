package ru.application.filmorate.controller;

import lombok.RequiredArgsConstructor;
import ru.application.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.application.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.getUserStorage().createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.getUserStorage().updateUser(user);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUserStorage().getUsers();
    }
}