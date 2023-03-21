package ru.application.filmorate.storage.friend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.model.User;
import ru.application.filmorate.util.Mapper;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendDao implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    public void addFriends(Integer id, Integer friendId) {
        String sql = "INSERT INTO FRIEND(USER1_ID, USER2_ID) VALUES (?, ?)";
        try {
            jdbcTemplate.update(sql, id, friendId);
            log.info("FriendDao addFriends(Integer id, Integer friendId): Пользователь с id = {} и id = {} стали друзьями.",
                    id, friendId);
        } catch (ObjectWasNotFoundException e) {
            String message = String.format("Пользователь с id = %d или id = %d не найден.", id, friendId);
            log.debug("FriendDao addFriends(Integer id, Integer friendId): Пользователь с id = {} или id = {} не найден.",
                    id, friendId);
            throw new ObjectWasNotFoundException(message);
        }
    }

    public void removeFriends(Integer id, Integer friendId) {
        String sql =
                "MERGE INTO FRIEND AS f USING (VALUES (?,?)) S(user1, user2)\n" +
                        "ON f.user1_ID = S.user1 AND f.user2_ID = S.user2 \n" +
                        "WHEN MATCHED THEN DELETE";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public List<User> getListOfFriendsSharedWithAnotherUser(Integer id, Integer otherId) {
        String sql =
                "SELECT * FROM USERS AS u, FRIEND AS f, FRIEND o " +
                        "WHERE u.ID = f.USER2_ID AND u.ID = o.USER2_ID AND f.USER1_ID = ? AND o.USER1_ID = ?";
        return jdbcTemplate.query(sql, Mapper::userMapper, id, otherId);
    }

    @Override
    public List<User> getListOfFriends(Integer id) {
        String sql =
                "SELECT U.ID, U.EMAIL, U.LOGIN, U.NAME, U.BIRTHDAY " +
                        "FROM FRIEND AS F " +
                        "LEFT JOIN USERS AS U ON F.USER2_ID = U.ID " +
                        "WHERE USER1_ID = ?";
        return jdbcTemplate.query(sql, Mapper::userMapper, id);
    }
}
