package ru.application.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.application.filmorate.exception.UserValidationException;
import ru.application.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserService {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 1;

    public User createUser(User userFromRequest) {
        log.info("Получен запрос 'POST /users'");
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

    public User updateUser(@Valid @RequestBody User userFromRequest) {
        log.info("Получен запрос 'PUT /users'");
        if (!users.containsKey(userFromRequest.getId())) {
            String exceptionMessage = "Такого пользователя нет в списке.";
            log.warn("Ошибка при обновлении пользователя. Текст исключения: {}", exceptionMessage);
            throw new UserValidationException(exceptionMessage);
        }
        User user = validationUser(userFromRequest);
        users.remove(userFromRequest.getId());
        users.put(user.getId(), user);
        log.info("Пользователь обновлен.");
        return user;
    }

    public List<User> getUsers() {
        log.info("Получен запрос 'GET /users'");
        log.debug("Текущее количество пользователей: {}", users.size());
        return List.copyOf(users.values());
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
}
