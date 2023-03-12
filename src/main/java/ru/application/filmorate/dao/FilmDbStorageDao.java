package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.model.Genre;
import ru.application.filmorate.model.Mpa;
import ru.application.filmorate.storage.FilmGenreStorage;
import ru.application.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorageDao implements FilmStorage, FilmGenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreStorage filmGenreStorage;
    private final LikeDao likeDao;
    private final MpaDao mpaDao;

    public List<Film> get() {
        String sql = "SELECT ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA, RATING FROM film";
        return getFilms(sql);
    }

    public Film getById(Integer filmId) {
        String sql =
                "SELECT ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA, RATING\n" +
                        "FROM FILM " +
                        "WHERE ID = ?";
        try {
            Film film = jdbcTemplate.queryForObject(sql, (ResultSet rs, int rowNum) -> makeFilm(rs), filmId);
            assert film != null;
            film.setGenres(get(filmId));
            film.setMpa(mpaDao.getById(film.getMpa().getId()));
            return film;
        } catch (EmptyResultDataAccessException e) {
            String message = String.format("Фильм с идентификатором %d не найден.", filmId);
            throw new ObjectWasNotFoundException(message);
        }
    }

    public List<Film> getPopularMoviesByLikes(Integer count) {
        String sql = String.format("SELECT id, name, description, release_date, duration, mpa, rating \n" +
                "FROM FILM\n" +
                "ORDER BY rating DESC\n" +
                "LIMIT %d", count
        );
        return getFilms(sql);
    }

    private List<Film> getFilms(String sql) {
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        films.forEach((film) -> {
            film.setGenres(get(film.getId()));
            film.setMpa(mpaDao.getById(film.getMpa().getId()));
        });
        return films;
    }

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
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return film;
        }
        insert(film);
        return film;
    }

    public Film update(Film film) {
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
        } else {
            removeById(film.getId());
            if (film.getGenres() == null || film.getGenres().isEmpty()) {
                return film;
            }
        }
        return insert(film);
    }

    public Film addLike(Integer id, Integer userId) {
        likeDao.addLike(id, userId);
        return getById(id);
    }


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

    @Override
    public List<Genre> get(int id) {
        return filmGenreStorage.get(id);
    }

    @Override
    public Film insert(Film film) {
        return filmGenreStorage.insert(film);
    }

    @Override
    public void removeById(int id) {
        filmGenreStorage.removeById(id);
    }
}