package ru.application.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.storage.LikeStorage;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LikeDao implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(int id, int userId) {
        try {
            jdbcTemplate.update("INSERT INTO LIKE_FILM (film_id, user_id) VALUES (?,?)", id, userId);
        } catch (ObjectWasNotFoundException e) {
            String message = String.format("Фильм с id = %d или пользователь с id = %d не найден.", id, userId);
            log.debug("addLike(int id, int userId): Фильм с id = {} или пользователь с id = {} не найден.",
                    id, userId);
            throw new ObjectWasNotFoundException(message);
        }
    }

    @Override
    public void remove(int id, int userId) {
        try {
            jdbcTemplate.update("DELETE FROM LIKE_FILM WHERE FILM_ID = ? AND USER_ID = ?", id, userId);
        } catch (ObjectWasNotFoundException e) {
            String message = String.format("Фильм с id = %d или Пользователь с id = %d не найден.", id, userId);
            log.debug("removeLike(int id, int userId): Фильм с id = {} или пользователь с id = {} не найден.",
                    id, userId);
            throw new ObjectWasNotFoundException(message);
        }
    }
}