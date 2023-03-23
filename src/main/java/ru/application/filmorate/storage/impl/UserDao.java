package ru.application.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.model.User;
import ru.application.filmorate.storage.util.Mapper;
import ru.application.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDao implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> get() {
        String sql = "SELECT ID, EMAIL, LOGIN, NAME, BIRTHDAY FROM USERS";
        return jdbcTemplate.query(sql, Mapper::userMapper);
    }

    @Override
    public User get(Integer userId) {
        try {
            User user = jdbcTemplate.queryForObject(
                    "SELECT id, email, login, name, birthday " +
                    "FROM users " +
                    "WHERE id = ?",
                    Mapper::userMapper, userId
            );
            if (user != null) {
                log.info("Найден пользователь: c id = {} именем = {}", user.getId(), user.getName());
            }
            return user;
        } catch (EmptyResultDataAccessException e) {
            String message = String.format("Пользователь с id = %d не найден.", userId);
            log.debug("getById(Integer userId): Пользователь с id = {} не найден.", userId);
            throw new ObjectWasNotFoundException(message);
        }
    }

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES (?, ?, ?, ?)",
                    new String[]{"id"}
            );
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setDate(4, Date.valueOf(user.getBirthday()));
            return preparedStatement;
        }, keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        int updateRows = jdbcTemplate.update(
                "UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE ID = ?",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId()
        );
        if (updateRows == 0) {
            String message = String.format("Пользователь с идентификатором %d не найден.", user.getId());
            log.debug("update(User user): Пользователь с идентификатором {} не найден.", user.getId());
            throw new ObjectWasNotFoundException(message);
        } else {
            return user;
        }
    }

    @Override
    public void remove(Integer id) {
        if (jdbcTemplate.update("DELETE FROM USERS WHERE ID = ? ", id) == 0) {
            String message = String.format("Пользователь с id = %d не найден.", id);
            log.debug("removeUserById(Integer id): Пользователь с id = {} не найден.", id);
            throw new ObjectWasNotFoundException(message);
        }
    }

    @Override
    public Boolean isExist(Integer userId) {
        return jdbcTemplate.queryForObject("SELECT EXISTS(SELECT * FROM USERS WHERE ID = ?)",
                Boolean.class, userId);
    }
}
