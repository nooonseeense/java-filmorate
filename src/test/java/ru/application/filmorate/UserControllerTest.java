//package ru.application.filmorate.controller;
//
//import org.junit.Test;
//import ru.application.filmorate.model.User;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class UserControllerTest extends UserController {
//    UserController userController = new UserController();
//
//    @Test
//    public void createUserTest() {
//        User user = User.builder()
//                .id(1)
//                .email("test@ya.ru")
//                .login("testuser")
//                .name("Boris")
//                .birthday(LocalDate.of(2000, 12, 26))
//                .build();
//
//        assertEquals(user, userController.createUser(user), "Пользователь не был создан.");
//        System.out.println(user.toString());
//    }
//
//    @Test
//    public void shouldChangeNameToLoginIfNameIsEmpty() {
//        User user = User.builder()
//                .id(1)
//                .email("test@ya.ru")
//                .login("testuser")
//                .name("")
//                .birthday(LocalDate.of(2000, 12, 26))
//                .build();
//        User newUser = userController.createUser(user);
//        assertEquals(user.getLogin(), newUser.getName(), "Имя не было изменено.");
//    }
//}
