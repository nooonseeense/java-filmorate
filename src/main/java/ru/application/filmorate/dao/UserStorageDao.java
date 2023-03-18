package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.model.User;
import ru.application.filmorate.impl.UserStorage;
import ru.application.filmorate.util.Mapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserStorageDao implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> get() {
        String sql = "SELECT ID, EMAIL, LOGIN, NAME, BIRTHDAY FROM USERS";
        return jdbcTemplate.query(sql, Mapper::userMapper);
    }

    @Override
    public User getById(Integer userId) {
        String sql =
                "SELECT id, email, login, name, birthday " +
                        "FROM users " +
                        "WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, Mapper::userMapper, userId);
            if (user != null) {
                log.info("Найден пользователь: c id = {} именем = {}", user.getId(), user.getName());
            }
            return user;
        } catch (EmptyResultDataAccessException e) {
            String message = String.format("Пользователь с id = %d не найден.", userId);
            log.debug(message);
            throw new ObjectWasNotFoundException(message);
        }
    }

    @Override
    public User create(User user) {
        String sqlQuery = "insert into USERS (EMAIL, login, name, birthday) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, new String[]{"id"});
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
        String sqlQuery =
                "UPDATE USERS SET " +
                        "EMAIL = ?, " +
                        "LOGIN = ?, " +
                        "NAME = ?, " +
                        "BIRTHDAY = ? " +
                        "WHERE ID = ?";
        int updateRows = jdbcTemplate.update(
                sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId()
        );
        if (updateRows == 0) {
            String message = String.format("Пользователь с идентификатором %d не найден.", user.getId());
            log.debug(message);
            throw new ObjectWasNotFoundException(message);
        } else {
            return user;
        }
    }

    @Override
    public void removeUserById(Integer id) {
        String sql = "DELETE FROM USERS  " +
                "WHERE ID = ? ";
        if (jdbcTemplate.update(sql, id) == 0) {
            String message = String.format("Пользователь с id = %d не найден.", id);
            log.debug(message);
            throw new ObjectWasNotFoundException(message);
        }
    }

    public Set<Integer> getMatchingUserIds(Integer userId) {
        log.debug("Получение списка идентификаторов пользователей, " +
                "лайкнувших те же фильмы, что и пользователь с id = {}", userId);
        String sql = "SELECT DISTINCT lf2.USER_ID " +
                "FROM LIKE_FILM lf1 " +
                "JOIN LIKE_FILM lf2 ON lf1.FILM_ID = lf2.FILM_ID " +
                "WHERE lf1.USER_ID = ? " +
                "AND lf2.USER_ID <> ?";

        return new HashSet<>(jdbcTemplate.queryForList(sql, Integer.class, userId, userId));
    }
}