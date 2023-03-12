package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.model.User;
import ru.application.filmorate.storage.FriendStorage;
import ru.application.filmorate.storage.UserStorage;

@Component
@RequiredArgsConstructor
@Slf4j
public class FriendDao implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    public User addFriends(Integer id, Integer friendId) {
        String sql = "INSERT INTO FRIEND(USER1_ID, USER2_ID) VALUES (?, ?)";
        try {
            jdbcTemplate.update(sql, id, friendId);
            log.info("Пользователь с id = {} и id = {} стали друзьями.", id, friendId);
            return userStorage.getById(id);
        } catch (ObjectWasNotFoundException e) {
            String message = String.format("Пользователь с id = %d или id = %d не найден.", id, friendId);
            log.debug(message);
            throw new ObjectWasNotFoundException(message);
        }
    }

    public User removeFriends(Integer id, Integer friendId) {
        String sql =
                "MERGE INTO FRIEND AS f USING (VALUES (?,?)) S(user1, user2)\n" +
                        "ON f.user1_ID = S.user1 AND f.user2_ID = S.user2 \n" +
                        "WHEN MATCHED THEN DELETE";
        jdbcTemplate.update(sql, id, friendId);
        return userStorage.getById(id);
    }
}
