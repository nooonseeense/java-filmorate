package ru.application.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.application.filmorate.exception.ObjectDoesNotExist;
import ru.application.filmorate.storage.LikeStorage;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LikeDao implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(int id, int userId) {
        try {
            jdbcTemplate.update("INSERT INTO LIKE_FILM (FILM_ID, USER_ID) VALUES (?,?)", id, userId);
        } catch (ObjectDoesNotExist e) {
            String message = String.format("Фильм с ID = %d или пользователь с id = %d не найден.", id, userId);
            log.debug("addLike(int id, int userId): Фильм с ID = {} или пользователь с ID = {} не найден.",
                    id, userId);
            throw new ObjectDoesNotExist(message);
        }
    }

    @Override
    public void remove(int id, int userId) {
        try {
            jdbcTemplate.update("DELETE FROM LIKE_FILM WHERE FILM_ID = ? AND USER_ID = ?", id, userId);
        } catch (ObjectDoesNotExist e) {
            String message = String.format("Фильм с ID = %d или Пользователь с ID = %d не найден.", id, userId);
            log.debug("removeLike(int id, int userId): Фильм с ID = {} или пользователь с ID = {} не найден.",
                    id, userId);
            throw new ObjectDoesNotExist(message);
        }
    }
}