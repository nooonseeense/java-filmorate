package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.impl.FilmGenreStorage;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.impl.FilmStorage;
import ru.application.filmorate.model.Genre;
import ru.application.filmorate.util.Mapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorageDao implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreStorage filmGenreStorage;

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
    public List<Film> getCommonMovies(Integer userId, Integer friendId) {
        String sql = "SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, m.ID, m.NAME " +
                "FROM FILM AS f " +
                "LEFT JOIN LIKE_FILM AS lf ON f.ID = lf.FILM_ID " +
                "LEFT JOIN MPA AS m ON m.ID = f.MPA " +
                "WHERE f.ID IN (SELECT f.ID FROM FILM AS f " +
                "LEFT JOIN LIKE_FILM AS lfu ON lfu.FILM_ID = f.ID " +
                "LEFT JOIN LIKE_FILM AS lff ON lff.FILM_ID = f.ID " +
                "WHERE lfu.USER_ID = ? AND lff.USER_ID = ?) " +
                "GROUP BY f.ID " +
                "ORDER BY COUNT(lf.FILM_ID) DESC";
        return jdbcTemplate.query(sql, Mapper::filmMapper, userId, friendId);
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

    @Override
    public void removeFilmById(Integer id) {
        String sql = "DELETE FROM FILM  " +
                "WHERE ID = ? ";
        if (jdbcTemplate.update(sql, id) == 0) {
            String message = String.format("Фильм с id = %d не найден.", id);
            log.debug(message);
            throw new ObjectWasNotFoundException(message);
        }
    }

    public List<Film> getRecommendedFilms(Integer userId) {
        String sql = "SELECT f.ID, f.NAME, m.ID, m.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION " +
                "FROM LIKE_FILM AS lf1 " +
                "JOIN LIKE_FILM AS lf2 ON lf2.FILM_ID = lf1.FILM_ID AND lf1.USER_ID = ? " +
                "JOIN LIKE_FILM AS lf3 ON lf3.USER_ID = lf2.USER_ID AND lf3.USER_ID <> ? " +
                "AND lf3.FILM_ID NOT IN (SELECT FILM_ID " +
                "FROM LIKE_FILM " +
                "WHERE USER_ID = ?) " +
                "JOIN FILM AS f ON f.ID = lf3.FILM_ID " +
                "JOIN MPA AS m ON f.MPA = m.ID " +
                "GROUP BY lf3.FILM_ID, f.ID " +
                "ORDER BY COUNT(*) DESC";

        List<Film> recommendedFilms = jdbcTemplate.query(sql, Mapper::filmMapper, userId, userId, userId);
        filmGenreStorage.setGenres(recommendedFilms);

        return recommendedFilms;
    }
}