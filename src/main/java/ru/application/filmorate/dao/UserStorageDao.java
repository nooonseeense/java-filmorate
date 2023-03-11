package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.model.User;
import ru.application.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
@Qualifier("InDbUserStorage")
public class UserStorageDao implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public static User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        return User.builder()
                .id(id)
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
    }

    @Override
    public List<User> get() {
        String sql = "SELECT ID, EMAIL, LOGIN, NAME, BIRTHDAY FROM USERS";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
        for (User user : users) {
            user.setFriendsId(convertToSetFromList(getListOfFriends(user.getId())));
        }
        return users;
    }

    @Override
    public User getById(Integer userId) {
        String sql =
                "SELECT id, email, login, name, birthday " +
                        "FROM users " +
                        "WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, (ResultSet rs, int rowNum) -> makeUser(rs), userId);
            if (user != null) {
                log.info("Найден пользователь: c id = {} именем = {}", user.getId(), user.getName());
                user.setFriendsId(convertToSetFromList(getListOfFriends(user.getId())));
            }
            return user;
        } catch (EmptyResultDataAccessException e) {
            String message = String.format("Пользователь с id = %d не найден.", userId);
            log.debug(message);
            throw new ObjectWasNotFoundException(message);
        }
    }

    @Override
    public List<User> getListOfFriendsSharedWithAnotherUser(Integer id, Integer otherId) {
        String sql =
                "SELECT u.id, u.email, u.login, u.name, u.birthday \n" +
                        "FROM friend AS f\n" +
                        "INNER JOIN users AS u ON f.user2_id = u.id\n" +
                        "WHERE user1_id = ? AND user2_id IN (\n" +
                        "        SELECT user2_id\n" +
                        "        FROM friend\n" +
                        "        WHERE user1_id = ?\n" +
                        "     )";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id, otherId);
    }

    @Override
    public List<User> getListOfFriends(Integer id) { // НЕПРАВИЛЬНЫЙ ЗАПРОУС
        String sql =
                "SELECT U.ID, U.EMAIL, U.LOGIN, U.NAME, U.BIRTHDAY " +
                        "FROM FRIEND AS F " +
                        "LEFT JOIN USERS AS U ON F.USER2_ID = U.ID " +
                        "WHERE USER1_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id);
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
    public User addFriends(Integer id, Integer friendId) {
        String sql =
                "MERGE INTO FRIEND AS f USING (VALUES (?,?)) S(user1, user2)\n" +
                "ON f.USER1_ID = S.user1 AND f.USER2_ID = S.user2 \n" +
                "WHEN NOT MATCHED THEN INSERT VALUES (S.user1, S.user2)";
        try {
            jdbcTemplate.update(sql, id, friendId);
            log.info("Пользователь с id = {} и id = {} стали друзьями.", id, friendId);
            return getById(id);
        } catch (ObjectNotFoundException e) {
            String message = String.format("Пользователь с id = %d или id = %d не найден.", id, friendId);
            log.debug(message);
            throw new ObjectWasNotFoundException(message);
        }
    }

    @Override
    public User removeFriends(Integer id, Integer friendId) {
        String sql =
                "MERGE INTO FRIEND AS f USING (VALUES (?,?)) S(user1, user2)\n" +
                "ON f.user1_ID = S.user1 AND f.user2_ID = S.user2 \n" +
                "WHEN MATCHED THEN DELETE";
        jdbcTemplate.update(sql, id, friendId);
        return getById(id);
    }

    private Set<Integer> convertToSetFromList(List<User> users) {
        Set<Integer> commonUsers = new HashSet<>();
        if (!users.isEmpty()) {
            for (User friend : users) {
                commonUsers.add(friend.getId());
            }
        }
        return commonUsers;
    }
}