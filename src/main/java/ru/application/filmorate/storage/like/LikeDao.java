package ru.application.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.application.filmorate.exception.ObjectWasNotFoundException;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeDao implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(int id, int userId) {
        String sql = "INSERT INTO LIKE_FILM (film_id, user_id) VALUES (?,?)";
        try {
            jdbcTemplate.update(sql, id, userId);
        } catch (ObjectWasNotFoundException e) {
            String message = String.format("Фильм с id = %d или пользователь с id = %d не найден.", id, userId);
            log.debug("LikeDao addLike(int id, int userId): Фильм с id = {} или пользователь с id = {} не найден.",
                    id, userId);
            throw new ObjectWasNotFoundException(message);
        }
    }

    @Override
    public void removeLike(int id, int userId) {
        String sql = "DELETE FROM LIKE_FILM  " +
                "WHERE FILM_ID = ? AND USER_ID = ?";
        try {
            jdbcTemplate.update(sql, id, userId);
        } catch (ObjectWasNotFoundException e) {
            String message = String.format("Фильм с id = %d или Пользователь с id = %d не найден.", id, userId);
            log.debug("LikeDao removeLike(int id, int userId): Фильм с id = {} или пользователь с id = {} не найден.",
                    id, userId);
            throw new ObjectWasNotFoundException(message);
        }
    }
}