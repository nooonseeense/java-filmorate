package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
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
@Qualifier("InDbFilmStorage")
public class FilmDbStorageDao implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreDao filmGenreDao;
    private final LikeDao likeDao;
    private final MpaDao mpaDao;

    @Override
    public List<Film> get() {
        String sql = "SELECT ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA, RATING, NUM_OF_LIKES FROM film";
        return getFilms(sql);
    }

    @Override
    public Film getById(Integer filmId) {
        String sql = "SELECT ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA, RATING, NUM_OF_LIKES\n" +
                "FROM FILM " +
                "WHERE ID = ?";
        try {
            Film film = jdbcTemplate.queryForObject(sql, (ResultSet rs, int rowNum) -> makeFilm(rs), filmId);
            assert film != null;
            film.setLikes(likeDao.getFilmLikes(filmId));
            film.setGenres(filmGenreDao.get(filmId));
            film.setMpa(mpaDao.getById(film.getMpa().getId()));
            return film;
        } catch (EmptyResultDataAccessException e) {
            String message = String.format("Фильм с идентификатором %d не найден.", filmId);
            throw new ObjectWasNotFoundException(message);
        }
    }

    @Override
    public List<Film> getPopularMoviesByLikes(Integer count) {
        String sql = String.format("SELECT id, name, description, release_date, duration, mpa, rating, num_of_likes \n" +
                "FROM FILM\n" +
                "ORDER BY NUM_OF_LIKES DESC\n" +
                "LIMIT %d", count
        );
        return getFilms(sql);
    }

    private List<Film> getFilms(String sql) {
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        films.forEach((film) -> {
            film.setLikes(likeDao.getFilmLikes(film.getId()));
            film.setGenres(filmGenreDao.get(film.getId()));
            film.setMpa(mpaDao.getById(film.getMpa().getId()));
        });
        return films;
    }

    @Override
    public Film add(Film film) {
        String sql = "INSERT INTO FILM (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA, RATING, NUM_OF_LIKES) " +
                "VALUES (?,?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setDate(3, Date.valueOf(film.getReleaseDate()));
            preparedStatement.setInt(4, film.getDuration());
            preparedStatement.setInt(5, film.getMpa().getId());
            preparedStatement.setInt(6, film.getRating());
            preparedStatement.setInt(7, 0);
            return preparedStatement;
        }, keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        film.setId(id);
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return film;
        }
        filmGenreDao.insert(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE FILM " +
                "SET NAME = ?, " +
                "description = ?, " +
                "release_date = ?, " +
                "duration = ?, " +
                "mpa = ?, " +
                "rating = ? " +
                " WHERE id = ?";
        int newRows = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getRating(),
                film.getId()
        );
        if (newRows == 0) {
            String message = String.format("Фильм с ID = %d не найден.", film.getId());
            log.debug(message);
            throw new ObjectWasNotFoundException(message);
        } else {
            filmGenreDao.removeById(film.getId());
            if (film.getGenres() == null || film.getGenres().isEmpty()) {
                return film;
            }
        }
        return filmGenreDao.insert(film);
    }

    @Override
    public Film addLike(Integer id, Integer userId) {
        likeDao.addLike(id, userId);
        return getById(id);
    }

    @Override
    public Film removeLike(Integer id, Integer userId) {
        likeDao.removeLike(id, userId);
        return getById(id);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        Mpa mpa = new Mpa(rs.getInt("mpa"));
        int rating = rs.getInt("rating");
        int numOfLikes = rs.getInt("NUM_OF_LIKES");
        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .rating(rating)
                .mpa(mpa)
                .numOfLikes(numOfLikes)
                .build();
    }
}