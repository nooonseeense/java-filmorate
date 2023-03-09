package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.model.User;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FriendDao {
    private final JdbcTemplate jdbcTemplate;

    public void addFriends(int user1, int user2) {
        String sql = "MERGE INTO friend f USING (VALUES (?,?)) S(user1, user2)\n" +
                "ON f.user1_ID = S.user1 AND f.user2_ID = S.user2 \n" +
                "WHEN NOT MATCHED THEN INSERT VALUES (S.user1, S.user2)";
        try {
            jdbcTemplate.update(sql, user1, user2);
            log.info("Пользователь с id = {} и id = {} стали друзьями.", user1, user2);
        } catch (ObjectNotFoundException e) {
            String message = String.format("Пользователь с id = %d или id = %d не найден.", user1, user2);
            log.debug(message);
            throw new ObjectWasNotFoundException(message);
        }
    }

    public void removeFriends(int user1, int user2) {
        String sql = "MERGE INTO friend f USING (VALUES (?,?)) S(user1, user2)\n" +
                "ON f.user1_ID = S.user1 AND f.user2_ID = S.user2 \n" +
                "WHEN MATCHED THEN DELETE";
        jdbcTemplate.update(sql, user1, user2);
    }

    public List<User> getListOfFriendsSharedWithAnotherUser(Integer id, Integer otherId) {
        String sql = "SELECT u.id, u.email, u.login, u.name, u.birthday \n" +
                "FROM friend AS f\n" +
                "INNER JOIN users AS u ON f.user2_id = u.id\n" +
                "WHERE user1_id = ? AND user2_id IN (\n" +
                "        SELECT user2_id\n" +
                "        FROM friend\n" +
                "        WHERE user1_id = ?\n" +
                "     )";
        return jdbcTemplate.query(sql, (rs, rowNum) -> UserDbStorageDao.makeUser(rs), id, otherId);
    }

    public List<User> getListOfFriends(Integer id) {
        String sql = "SELECT u.id, u.email, u.login, u.name, u.birthday " +
                "FROM friend AS f " +
                "LEFT JOIN users AS u on f.user2_id = u.id " +
                "WHERE user1_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> UserDbStorageDao.makeUser(rs), id);
    }
}
