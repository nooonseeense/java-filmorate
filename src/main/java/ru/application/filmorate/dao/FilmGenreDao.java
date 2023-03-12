package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.model.Genre;
import ru.application.filmorate.storage.FilmGenreStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class FilmGenreDao implements FilmGenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> get(int id) {
        String sql =
                "SELECT g.id, g.name " +
                "FROM film_genre fg " +
                "LEFT JOIN genre g ON fg.genre_id = g.id " +
                "WHERE fg.film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> GenreDao.makeGenre(rs), id);
    }

    @Override
    public Film insert(Film film) {
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
        film.setGenres(genres);
        return film;
    }

    @Override
    public void removeById(int id) {
        String sql = "DELETE FROM FILM_GENRE WHERE film_id = ?";
        jdbcTemplate.update(sql, id);
    }
}