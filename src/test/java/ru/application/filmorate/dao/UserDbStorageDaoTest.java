package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.application.filmorate.model.User;
import ru.application.filmorate.storage.user.UserStorageDao;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageDaoTest {
    private final UserStorageDao userStorageDao;

    @Test
    @Sql(scripts = "file:src/test/resources/schema-tst.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "file:src/test/resources/data-tst.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void shouldReturnUser() {
        User userExpected = User.builder()
                .id(1)
                .email("test@ya.com")
                .login("testy")
                .name("Tree")
                .birthday(LocalDate.of(2021, 10, 10))
                .build();

        User userActual = userStorageDao.getById(userExpected.getId());

        assertEquals(userExpected.getId(), userActual.getId());
        assertEquals(userExpected.getEmail(), userActual.getEmail());
        assertEquals(userExpected.getLogin(), userActual.getLogin());
        assertEquals(userExpected.getName(), userActual.getName());
        assertEquals(userExpected.getBirthday(), userActual.getBirthday());
    }

    @Test
    @Sql(scripts = "file:src/test/resources/schema-tst.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "file:src/test/resources/data-tst.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void shouldReturnNewUser() {
        User user = User.builder()
                .email("testy@ya.com")
                .login("testyy")
                .name("Treee")
                .birthday(LocalDate.of(2021, 10, 10))
                .build();

        userStorageDao.create(user);
        User userActual = userStorageDao.getById(user.getId());

        assertEquals(user.getId(), userActual.getId());
        assertEquals(user.getEmail(), userActual.getEmail());
        assertEquals(user.getLogin(), userActual.getLogin());
        assertEquals(user.getName(), userActual.getName());
        assertEquals(user.getBirthday(), userActual.getBirthday());
    }

    @Test
    @Sql(scripts = "file:src/test/resources/schema-tst.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "file:src/test/resources/data-tst.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void shouldReturnUsers() {
        User user = User.builder()
                .id(1)
                .email("test@ya.com")
                .login("testy")
                .name("Tree")
                .birthday(LocalDate.of(2021, 10, 10))
                .build();

        User user2 = User.builder()
                .id(2)
                .email("test@gu.com")
                .login("testg")
                .name("Bree")
                .birthday(LocalDate.of(2020, 10, 10))
                .build();

        List<User> actualList = userStorageDao.get();

        assertEquals(user.getId(), actualList.get(0).getId());
        assertEquals(user.getEmail(), actualList.get(0).getEmail());
        assertEquals(user.getLogin(), actualList.get(0).getLogin());
        assertEquals(user.getName(), actualList.get(0).getName());
        assertEquals(user.getBirthday(), actualList.get(0).getBirthday());

        assertEquals(user2.getId(), actualList.get(1).getId());
        assertEquals(user2.getEmail(), actualList.get(1).getEmail());
        assertEquals(user2.getLogin(), actualList.get(1).getLogin());
        assertEquals(user2.getName(), actualList.get(1).getName());
        assertEquals(user2.getBirthday(), actualList.get(1).getBirthday());
    }

    @Test
    @Sql(scripts = "file:src/test/resources/schema-tst.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "file:src/test/resources/data-tst.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void shouldReturnUpdateUser() {
        User user = User.builder()
                .id(1)
                .email("update@ya.com")
                .login("update_login")
                .name("update_name")
                .birthday(LocalDate.of(2022, 11, 11))
                .build();

        userStorageDao.update(user);
        User actualUser = userStorageDao.getById(user.getId());

        assertEquals(user.getId(), actualUser.getId());
        assertEquals(user.getEmail(), actualUser.getEmail());
        assertEquals(user.getLogin(), actualUser.getLogin());
        assertEquals(user.getName(), actualUser.getName());
        assertEquals(user.getBirthday(), actualUser.getBirthday());
    }
}