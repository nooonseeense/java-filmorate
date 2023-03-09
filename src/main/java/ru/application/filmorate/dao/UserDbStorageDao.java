package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.application.filmorate.model.User;
import ru.application.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Qualifier("InDbUserStorage")
public class UserDbStorageDao implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FriendDao friendShipDao;

    public static User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        return User.builder()
                .id(id)
                .email(email)
                .login(login)
                .birthday(birthday)
                .build();
    }

    @Override
    public List<User> get() {
        return null;
    }

    @Override
    public User getById(Integer userId) {
        return null;
    }

    @Override
    public List<User> getListOfFriendsSharedWithAnotherUser(Integer id, Integer otherId) {
        return null;
    }

    @Override
    public List<User> getListOfFriends(Integer id) {
        return null;
    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public User addFriends(Integer id, Integer friendId) {
        return null;
    }

    @Override
    public User removeFriends(Integer id, Integer friendId) {
        return null;
    }
}
