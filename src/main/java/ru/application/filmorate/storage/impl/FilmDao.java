package ru.application.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.application.filmorate.exception.ObjectDoesNotExist;
import ru.application.filmorate.model.Director;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.model.Genre;
import ru.application.filmorate.storage.FilmStorage;
import ru.application.filmorate.storage.FilmGenreStorage;
import ru.application.filmorate.storage.util.Mapper;
import ru.application.filmorate.enumeration.FilmSortType;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.application.filmorate.constant.Constants.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FilmDao implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreStorage filmGenreStorage;

    @Override
    public List<Film> get() {
        return jdbcTemplate.query(
                "SELECT f.*, m.* " +
                "FROM FILM f " +
                "JOIN MPA m ON m.ID = f.MPA",
                Mapper::filmMapper
        );
    }

    @Override
    public Film get(Integer filmId) {
        return jdbcTemplate.queryForObject(
                "SELECT f.*, m.* " +
                "FROM FILM f " +
                "JOIN MPA m ON m.ID = f.MPA " +
                "WHERE f.ID = ?",
                Mapper::filmMapper, filmId
        );
    }

    @Override
    public List<Film> getPopularMovies(String query, String by) {
        StringBuilder sql = new StringBuilder(
                "SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, m.ID, m.NAME " +
                "FROM FILM f " +
                "LEFT JOIN LIKE_FILM lf ON f.ID = lf.FILM_ID " +
                "LEFT JOIN MPA m ON m.ID = f.MPA " +
                "LEFT JOIN FILM_DIRECTOR fd ON f.ID = fd.FILM_ID " +
                "LEFT JOIN DIRECTOR d ON fd.DIRECTOR_ID = d.ID "
        );
        if (by.equals(TITLE)) {
            sql.append("WHERE LOWER(f.NAME) LIKE LOWER('%").append(query).append("%') ");
        }
        if (by.equals(DIRECTOR)) {
            sql.append("WHERE LOWER(d.NAME) LIKE LOWER('%").append(query).append("%') ");
        }
        if (by.equals(DIRECTOR_AND_TITLE) || by.equals(TITLE_AND_DIRECTOR)) {
            sql.append("WHERE LOWER(f.NAME) LIKE LOWER('%").append(query).append("%') OR ");
            sql.append("LOWER(d.NAME) LIKE LOWER('%").append(query).append("%') ");
        }
        sql.append("GROUP BY f.ID, lf.FILM_ID " +
                   "ORDER BY COUNT(lf.film_id) DESC");
        return jdbcTemplate.query(sql.toString(), Mapper::filmMapper);
    }

    public List<Film> getPopularMovies(Integer count, Integer genreId, Short year) {
        if (genreId == null && year == null) {
            return jdbcTemplate.query(
                    "SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, m.ID, m.NAME " +
                            "FROM FILM f " +
                            "LEFT JOIN LIKE_FILM lf ON f.ID = lf.FILM_ID " +
                            "LEFT JOIN MPA m ON m.ID = f.MPA " +
                            "GROUP BY f.ID, lf.FILM_ID "+
                            "ORDER BY COUNT(lf.film_id) DESC " +
                            "LIMIT ?",
                    Mapper::filmMapper, count
            );
        }

        if (genreId != null && year != null) {
            return jdbcTemplate.query(
                    "SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, m.ID, m.NAME " +
                            "FROM FILM f " +
                            "LEFT JOIN LIKE_FILM lf ON f.ID = lf.FILM_ID " +
                            "LEFT JOIN MPA m ON m.ID = f.MPA " +
                            "LEFT JOIN FILM_GENRE fg ON f.ID = fg.FILM_ID " +
                            "WHERE fg.GENRE_ID = ? AND YEAR(f.RELEASE_DATE) = ? " +
                            "GROUP BY f.ID, lf.FILM_ID " +
                            "ORDER BY COUNT(lf.film_id) DESC " +
                            "LIMIT ?",
                    Mapper::filmMapper, genreId, year, count
            );
        }

        if (genreId != null) {
            return jdbcTemplate.query(
                    "SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, m.ID, m.NAME " +
                            "FROM FILM f " +
                            "LEFT JOIN LIKE_FILM lf ON f.ID = lf.FILM_ID " +
                            "LEFT JOIN MPA m ON m.ID = f.MPA " +
                            "LEFT JOIN FILM_GENRE fg ON f.ID = fg.FILM_ID " +
                            "WHERE fg.GENRE_ID = ? " +
                            "GROUP BY f.ID, lf.FILM_ID " +
                            "ORDER BY COUNT(lf.film_id) DESC " +
                            "LIMIT ?",
                    Mapper::filmMapper, genreId, count
            );
        }

        return jdbcTemplate.query(
                "SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, m.ID, m.NAME " +
                        "FROM FILM f " +
                        "LEFT JOIN LIKE_FILM lf ON f.ID = lf.FILM_ID " +
                        "LEFT JOIN MPA m ON m.ID = f.MPA " +
                        "WHERE YEAR(f.RELEASE_DATE) = ? " +
                        "GROUP BY f.ID, lf.FILM_ID " +
                        "ORDER BY COUNT(lf.film_id) DESC " +
                        "LIMIT ?",
                Mapper::filmMapper, year, count
        );
    }

    @Override
    public List<Film> getCommonMovies(Integer userId, Integer friendId) {
        return jdbcTemplate.query(
                "SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, m.ID, m.NAME, COUNT(lf.FILM_ID) " +
                        "FROM FILM f " +
                        "LEFT JOIN LIKE_FILM lf ON f.ID = lf.FILM_ID " +
                        "LEFT JOIN MPA m ON m.ID = f.MPA " +
                        "LEFT JOIN LIKE_FILM lfu ON lfu.FILM_ID = f.ID AND lfu.USER_ID = ? " +
                        "LEFT JOIN LIKE_FILM lff ON lff.FILM_ID = f.ID AND lff.USER_ID = ? " +
                        "WHERE lfu.USER_ID IS NOT NULL AND lff.USER_ID IS NOT NULL " +
                        "GROUP BY f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, m.ID, m.NAME " +
                        "ORDER BY COUNT(lf.FILM_ID) DESC",
                Mapper::filmMapper, userId, friendId
        );
    }

    @Override
    public List<Film> get(int directorId, FilmSortType sortBy) {
        switch (sortBy) {
            case year:
                return jdbcTemplate.query(
                        "SELECT * " +
                                "FROM FILM f " +
                                "LEFT JOIN MPA m ON f.MPA = m.ID " +
                                "LEFT JOIN FILM_DIRECTOR fd ON f.ID = fd.FILM_ID " +
                                "LEFT JOIN DIRECTOR d ON fd.DIRECTOR_ID = d.ID " +
                                "WHERE d.ID = ? " +
                                "GROUP BY f.ID " +
                                "ORDER BY f.RELEASE_DATE",
                        Mapper::filmMapper, directorId
                );
            case likes:
                return jdbcTemplate.query(
                        "SELECT * " +
                                "FROM FILM f " +
                                "LEFT JOIN LIKE_FILM lf ON f.ID = lf.FILM_ID " +
                                "LEFT JOIN MPA m ON f.MPA = m.ID " +
                                "LEFT JOIN FILM_DIRECTOR fd ON f.ID = fd.FILM_ID " +
                                "LEFT JOIN DIRECTOR d ON fd.DIRECTOR_ID = d.ID " +
                                "WHERE d.ID = ? " +
                                "GROUP BY f.ID " +
                                "ORDER BY COUNT(lf.FILM_ID) DESC",
                        Mapper::filmMapper, directorId
                );
            default:
                return new ArrayList<>();
        }
    }

    @Override
    public Film add(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement rs = connection.prepareStatement(
                    "INSERT INTO FILM (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA) " +
                    "VALUES (?,?,?,?,?)",
                    new String[]{"id"}
            );
            rs.setString(1, film.getName());
            rs.setString(2, film.getDescription());
            rs.setDate(3, Date.valueOf(film.getReleaseDate()));
            rs.setLong(4, film.getDuration());
            rs.setInt(5, film.getMpa().getId());
            return rs;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        set(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        removeGenres(film);
        removeDirectors(film);
        int newRows = jdbcTemplate.update(
                "UPDATE FILM SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA = ? WHERE ID = ?",
                film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId()
        );

        if (newRows == 0) {
            String message = String.format("Фильм с ID = %d не найден.", film.getId());
            log.debug("update(Film film): Фильм с ID = {} не найден.", film.getId());
            throw new ObjectDoesNotExist(message);
        }
        set(film);
        return film;
    }

    @Override
    public void addGenres(Film film) {
        List<Genre> genres = film.getGenres()
                .stream()
                .distinct()
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate("INSERT INTO FILM_GENRE(FILM_ID, GENRE_ID) VALUES(?, ?)",
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
    public void addDirectors(Film film) {
        List<Director> directors = film.getDirectors()
                .stream()
                .distinct()
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate("INSERT INTO FILM_DIRECTOR(FILM_ID, DIRECTOR_ID) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, film.getId());
                        ps.setInt(2, directors.get(i).getId());
                    }

                    public int getBatchSize() {
                        return directors.size();
                    }
                });
        film.getDirectors().clear();
    }

    @Override
    public void removeGenres(Film film) {
        jdbcTemplate.update("DELETE FROM FILM_GENRE WHERE FILM_ID = ?", film.getId());
    }

    @Override
    public void removeDirectors(Film film) {
        jdbcTemplate.update("DELETE FROM FILM_DIRECTOR WHERE FILM_ID = ?", film.getId());
    }

    @Override
    public void remove(Integer id) {
        if (jdbcTemplate.update("DELETE FROM FILM WHERE ID = ? ", id) == 0) {
            String message = String.format("Фильм с ID = %d не найден.", id);
            log.debug("update(Film film): Фильм с ID = {} не найден.", id);
            throw new ObjectDoesNotExist(message);
        }
    }

    @Override
    public Boolean isExist(Integer filmId) {
        return jdbcTemplate.queryForObject("SELECT EXISTS(SELECT * FROM FILM WHERE ID = ?)",
                Boolean.class, filmId);
    }

    public List<Film> getRecommendedFilms(Integer userId) {
        List<Film> films = jdbcTemplate.query(
                "SELECT f.ID, f.NAME, m.ID, m.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION " +
                        "FROM LIKE_FILM lf1 " +
                        "JOIN LIKE_FILM lf2 ON lf2.FILM_ID = lf1.FILM_ID AND lf1.USER_ID = ? " +
                        "JOIN LIKE_FILM lf3 ON lf3.USER_ID = lf2.USER_ID AND lf3.USER_ID <> ? " +
                        "LEFT JOIN LIKE_FILM lf4 ON lf4.USER_ID = ? AND lf4.FILM_ID = lf3.FILM_ID " +
                        "JOIN FILM f ON f.ID = lf3.FILM_ID " +
                        "JOIN MPA m ON f.MPA = m.ID " +
                        "WHERE lf4.ID IS NULL " +
                        "GROUP BY lf3.FILM_ID, f.ID " +
                        "ORDER BY COUNT(*) DESC",
                Mapper::filmMapper, userId, userId, userId
        );

        filmGenreStorage.set(films);
        return films;
    }

    private void set(Film film) {
        if (film.getGenres() != null) {
            addGenres(film);
        } else {
            film.setGenres(new LinkedHashSet<>());
        }

        if (film.getDirectors() != null) {
            addDirectors(film);
        } else {
            film.setDirectors(new LinkedHashSet<>());
        }
    }
}
