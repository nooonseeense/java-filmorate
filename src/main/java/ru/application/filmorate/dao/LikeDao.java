package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.impl.LikeStorage;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeDao implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(int id, int userId) {
        String sql = "INSERT INTO LIKE_FILM (film_id, user_id) VALUES (?,?)";
        try {
            jdbcTemplate.update(sql, id, userId);
        } catch (ObjectWasNotFoundException e) {
            String message = String.format("Фильм с id = %d или пользователь с id = %d не найден.", id, userId);
            log.debug(message);
            throw new ObjectWasNotFoundException(message);
        }
        updateRate(id);
    }

    @Override
    public void removeLike(int id, int userId) {
        String sql = "DELETE FROM LIKE_FILM  " +
                "WHERE FILM_ID = ? AND USER_ID = ?";
        try {
            jdbcTemplate.update(sql, id, userId);
        } catch (ObjectWasNotFoundException e) {
            String message = String.format("Фильм с id = %d или Пользователь с id = %d не найден.", id, userId);
            log.debug(message);
            throw new ObjectWasNotFoundException(message);
        }
        updateRate(id);
    }

    private void updateRate(long filmId) {
        String sql = "UPDATE FILM f SET RATING = (SELECT count(l.user_id) " +
                "FROM LIKE_FILM AS l where l.film_id = f.ID) " +
                "WHERE ID = ?";
        jdbcTemplate.update(sql, filmId);
    }
}