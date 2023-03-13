package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.model.Mpa;
import ru.application.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorageDao implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public String get() {
        return "SELECT F.ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, F.MPA, M.NAME, F.RATING " +
                "FROM FILM AS F " +
                "LEFT JOIN MPA AS M ON F.MPA = M.ID";
    }

    @Override
    public Film getById(Integer filmId) {
        String sql =
                "SELECT F.ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, F.MPA, M.NAME, F.RATING " +
                        "FROM FILM AS F " +
                        "LEFT JOIN MPA AS M ON F.MPA = M.ID " +
                        "WHERE F.ID = ?";
        return jdbcTemplate.queryForObject(sql, (ResultSet rs, int rowNum) -> makeFilm(rs), filmId);
    }

    @Override
    public String getPopularMoviesByLikes(Integer count) {
        return String.format("SELECT F.ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, F.MPA, M.NAME, F.RATING " +
                "FROM FILM AS F " +
                "LEFT JOIN MPA AS M ON F.MPA = M.ID " +
                "ORDER BY rating DESC " +
                "LIMIT %d", count
        );
    }

    @Override
    public List<Film> getFilms(String sql) {
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film add(Film film) {
        String sql = "INSERT INTO FILM (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA) " +
                "VALUES (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setDate(3, Date.valueOf(film.getReleaseDate()));
            preparedStatement.setInt(4, film.getDuration());
            preparedStatement.setInt(5, film.getMpa().getId());
            return preparedStatement;
        }, keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        film.setId(id);
        return film;
    }

    @Override
    public void update(Film film) {
        String sql = "UPDATE FILM " +
                "SET NAME = ?, " +
                "description = ?, " +
                "release_date = ?, " +
                "duration = ?, " +
                "mpa = ? " +
                "WHERE id = ?";
        int newRows = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        if (newRows == 0) {
            String message = String.format("Фильм с ID = %d не найден.", film.getId());
            log.debug(message);
            throw new ObjectWasNotFoundException(message);
        }
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        Mpa mpa = new Mpa(rs.getInt("mpa"));
        int rating = rs.getInt("rating");
        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .rating(rating)
                .mpa(mpa)
                .build();
    }
}