package ru.application.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.application.filmorate.exception.ObjectDoesNotExist;
import ru.application.filmorate.model.User;
import ru.application.filmorate.storage.FriendStorage;
import ru.application.filmorate.storage.util.Mapper;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FriendDao implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    public void addFriends(Integer id, Integer friendId) {
        try {
            jdbcTemplate.update("INSERT INTO FRIEND(USER1_ID, USER2_ID) VALUES (?, ?)", id, friendId);
            log.info("addFriends(Integer id, Integer friendId): Пользователь с id = {} и id = {} стали друзьями.",
                    id, friendId);
        } catch (ObjectDoesNotExist e) {
            String message = String.format("Пользователь с id = %d или id = %d не найден.", id, friendId);
            log.debug("addFriends(Integer id, Integer friendId): Пользователь с id = {} или id = {} не найден.",
                    id, friendId);
            throw new ObjectDoesNotExist(message);
        }
    }

    public void removeFriends(Integer id, Integer friendId) {
        jdbcTemplate.update(
                "MERGE INTO FRIEND AS f USING (VALUES (?,?)) S(user1, user2)\n" +
                "ON f.user1_ID = S.user1 AND f.user2_ID = S.user2 \n" +
                "WHEN MATCHED THEN DELETE",
                id, friendId
        );
    }

    @Override
    public List<User> getListOfFriendsSharedWithAnotherUser(Integer id, Integer otherId) {
        return jdbcTemplate.query(
                "SELECT * FROM USERS AS u, FRIEND AS f, FRIEND o " +
                "WHERE u.ID = f.USER2_ID AND u.ID = o.USER2_ID AND f.USER1_ID = ? AND o.USER1_ID = ?",
                Mapper::userMapper, id, otherId
        );
    }

    @Override
    public List<User> getListOfFriends(Integer id) {
        return jdbcTemplate.query(
                "SELECT U.ID, U.EMAIL, U.LOGIN, U.NAME, U.BIRTHDAY " +
                "FROM FRIEND AS F " +
                "LEFT JOIN USERS AS U ON F.USER2_ID = U.ID " +
                "WHERE USER1_ID = ?",
                Mapper::userMapper, id
        );
    }
}
