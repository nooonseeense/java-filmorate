package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.model.User;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeDao {
    private final UserDbStorageDao userDbStorageDao;
    private final JdbcTemplate jdbcTemplate;

    public void addLike(int id, int userId) {
        String sql = "INSERT INTO LIKE_FILM (film_id, user_id) VALUES (?,?)";
        String sql2 = "UPDATE FILM\n" +
                "SET NUM_OF_LIKES = FILM.NUM_OF_LIKES + 1\n" +
                "WHERE ID = ?";
        try {
            jdbcTemplate.update(sql, id, userId);
        } catch (ObjectWasNotFoundException e) {
            String message = String.format("Фильм с id = %d или пользователь с id = %d не найден.", id, userId);
            log.debug(message);
            throw new ObjectWasNotFoundException(message);
        }
        jdbcTemplate.update(sql2, id);
    }

    public void removeLike(int id, int userId) {
        String sql = "DELETE FROM LIKE_FILM  " +
                "WHERE FILM_ID = ? AND USER_ID = ?";
        String sql2 = "UPDATE FILM\n" +
                "SET NUM_OF_LIKES = NUM_OF_LIKES - 1\n" +
                "WHERE ID = ?";
        try {
            jdbcTemplate.update(sql, id, userId);
        } catch (ObjectWasNotFoundException e) {
            String message = String.format("Фильм с id = %d или Пользователь с id = %d не найден.", id, userId);
            log.debug(message);
            throw new ObjectWasNotFoundException(message);
        }
        jdbcTemplate.update(sql2, id);
    }

    public Set<Integer> getFilmLikes(int id) {
        String sql = "SELECT  UF.id, UF.email, UF.login, UF.name, UF.birthday " +
                "FROM LIKE_FILM l " +
                "LEFT JOIN USERS UF on l.USER_ID = UF.ID " +
                "WHERE film_id = ? ";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> UserDbStorageDao.makeUser(rs), id);
        Set<Integer> userIds = new HashSet<>();
        for (User user : users) {
            userIds.add(user.getId());
        }
        return userIds;
    }
}