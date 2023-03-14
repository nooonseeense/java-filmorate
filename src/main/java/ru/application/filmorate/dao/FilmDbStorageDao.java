package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.impl.FilmStorage;
import ru.application.filmorate.model.Genre;
import ru.application.filmorate.util.Mapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorageDao implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> get() {
        String sql = "SELECT FILM.*, m.* " +
                "FROM FILM " +
                "JOIN MPA m ON m.ID = FILM.MPA";
        return jdbcTemplate.query(sql, Mapper::filmMapper);
    }

    @Override
    public Film getById(Integer filmId) {
        String sql = "SELECT FILM.*, M.* " +
                "FROM FILM " +
                "JOIN MPA M ON M.ID = FILM.MPA " +
                "WHERE FILM.ID = ?";
        return jdbcTemplate.queryForObject(sql, Mapper::filmMapper, filmId);
    }

    @Override
    public List<Film> getPopularMoviesByLikes(Integer count) {
        String sql = "SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, m.ID, m.NAME " +
                "FROM FILM as f " +
                "LEFT JOIN LIKE_FILM lf ON f.ID = lf.FILM_ID " +
                "LEFT JOIN MPA m on m.ID = f.MPA " +
                "GROUP BY f.ID, lf.FILM_ID IN ( " +
                "SELECT FILM_ID " +
                "FROM LIKE_FILM) " +
                "ORDER BY COUNT(lf.film_id) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, Mapper::filmMapper, count);
    }

    @Override
    public Film add(Film film) {
        String sql = "INSERT INTO FILM (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA) " +
                "VALUES (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement rs = connection.prepareStatement(sql, new String[]{"id"});
            rs.setString(1, film.getName());
            rs.setString(2, film.getDescription());
            rs.setDate(3, Date.valueOf(film.getReleaseDate()));
            rs.setLong(4, film.getDuration());
            rs.setInt(5, film.getMpa().getId());
            return rs;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        if (film.getGenres() != null) {
            addGenres(film);
        } else {
            film.setGenres(new LinkedHashSet<>());
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE FILM SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA = ? WHERE ID = ?";
        removeGenres(film);
        if (film.getGenres() != null) {
            addGenres(film);
        }
        int newRows = jdbcTemplate.update(sql,
                film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        if (newRows == 0) {
            String message = String.format("Фильм с ID = %d не найден.", film.getId());
            log.debug(message);
            throw new ObjectWasNotFoundException(message);
        }
        return film;
    }

    @Override
    public void addGenres(Film film) {
        List<Genre> genres = film.getGenres()
                .stream()
                .distinct()
                .collect(Collectors.toList());
        String sql = "INSERT INTO FILM_GENRE(FILM_ID, GENRE_ID) VALUES(?,?)";
        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, film.getId());
                        ps.setInt(2, genres.get(i).getId());
                    }

                    public int getBatchSize() {
                        return genres.size();
                    }
                });
        film.getGenres().clear();
    }

    @Override
    public void removeGenres(Film film) {
        String sql = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getId());
    }
}