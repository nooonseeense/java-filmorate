package ru.application.filmorate.util;

import ru.application.filmorate.model.*;
import ru.application.filmorate.util.enumeration.EventType;
import ru.application.filmorate.util.enumeration.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashSet;

public class Mapper {

    public static User userMapper(ResultSet rs, int row) throws SQLException {
        int id = rs.getInt("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        return User.builder()
                .id(id)
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
    }

    public static Film filmMapper(ResultSet rs, int row) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        Mpa mpa = new Mpa(rs.getInt("mpa.id"), rs.getString("mpa.name"));
        int duration = rs.getInt("duration");
        return new Film(id, name, description, releaseDate, mpa, duration, new LinkedHashSet<>(), new LinkedHashSet<>());
    }

    public static Mpa mpaMapper(ResultSet rs, int row) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        return new Mpa(id, name);
    }

    public static Genre genreMapper(ResultSet rs, int row) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }

    public static Director directorMapper(ResultSet rs, int row) throws SQLException {
        return Director.builder().
                id(rs.getInt("id")).
                name(rs.getString("name")).build();
    }

    public static LikeFilm likeFilmMapper(ResultSet rs, int row) throws SQLException {
        return LikeFilm.builder()
                .id(rs.getInt("id"))
                .filmId(rs.getInt("film_id"))
                .userId(rs.getInt("user_id"))
                .build();
    }

    public static Review reviewMapper(ResultSet rs, int row) throws SQLException {
        return Review.builder()
                .reviewId(rs.getInt("id"))
                .content(rs.getString("content"))
                .isPositive(rs.getBoolean("is_positive"))
                .userId(rs.getInt("user_id"))
                .filmId(rs.getInt("film_id"))
                .useful(rs.getInt("useful"))
                .build();
    }

    public static Event eventMapper(ResultSet rs, int row) throws SQLException {
        return Event.builder()
                .eventId(rs.getInt("event_id"))
                .timestamp(rs.getTimestamp("time_stamp"))
                .userId(rs.getInt("user_id"))
                .eventType(EventType.valueOf(rs.getString("event_type")))
                .operation(Operation.valueOf(rs.getString("event_operation")))
                .entityId(rs.getInt("entity_id"))
                .build();
    }
}