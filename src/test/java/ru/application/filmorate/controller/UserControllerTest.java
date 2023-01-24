package ru.application.filmorate.controller;

import org.junit.Test;
import ru.application.filmorate.exception.UserValidationException;
import ru.application.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest extends UserController {
    UserController userController = new UserController();

    @Test
    public void createUserTest() {
        User user = User.builder()
                .id(1)
                .email("test@ya.ru")
                .login("testuser")
                .name("Boris")
                .birthday(LocalDate.of(2000, 12, 26))
                .build();

        assertEquals(user, userController.createUser(user), "Пользователь не был создан.");
    }

    @Test
    public void shouldReturnAnExceptionWhenCreateUserIfThereIsAnEmail() {
        User user = User.builder()
                .id(1)
                .email("testya.ru")
                .login("testuser")
                .name("Boris")
                .birthday(LocalDate.of(2000, 12, 26))
                .build();
        final UserValidationException ex = assertThrows(UserValidationException.class, () -> userController.createUser(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", ex.getMessage());
        User user2 = User.builder()
                .id(1)
                .email(" ")
                .login("testuser")
                .name("Boris")
                .birthday(LocalDate.of(2000, 12, 26))
                .build();
        ;
        final UserValidationException ex2 = assertThrows(UserValidationException.class, () -> userController.createUser(user2));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", ex2.getMessage());
    }

    @Test
    public void shouldReturnExceptionOnUserCreationIfLoginIsEmptyOrSpace() {
        User user = User.builder()
                .id(1)
                .email("test@ya.ru")
                .login(" ")
                .name("Boris")
                .birthday(LocalDate.of(2000, 12, 26))
                .build();
        final UserValidationException ex = assertThrows(UserValidationException.class, () -> userController.createUser(user));
        assertEquals("Логин не может быть пустым и содержать пробелы.", ex.getMessage());
        User user2 = User.builder()
                .id(1)
                .email("test@ya.ru")
                .login(null)
                .name("Boris")
                .birthday(LocalDate.of(2000, 12, 26))
                .build();
        final UserValidationException ex2 = assertThrows(UserValidationException.class, () -> userController.createUser(user2));
        assertEquals("Логин не может быть пустым и содержать пробелы.", ex2.getMessage());
    }

    @Test
    public void shouldReturnExceptionOnUserCreationIfTheBirthdayIsInTheFuture() {
        User user = User.builder()
                .id(1)
                .email("test@ya.ru")
                .login("testuser")
                .name("Boris")
                .birthday(LocalDate.of(2023, 12, 26))
                .build();
        ;
        final UserValidationException ex = assertThrows(UserValidationException.class, () -> userController.createUser(user));
        assertEquals("Дата рождения не может быть в будущем.", ex.getMessage());
    }

    @Test
    public void shouldChangeNameToLoginIfNameIsEmpty() {
        User user = User.builder()
                .id(1)
                .email("test@ya.ru")
                .login("testuser")
                .name("")
                .birthday(LocalDate.of(2000, 12, 26))
                .build();
        User newUser = userController.createUser(user);
        assertEquals(user.getLogin(), newUser.getName(), "Имя не было изменено.");
    }
}
