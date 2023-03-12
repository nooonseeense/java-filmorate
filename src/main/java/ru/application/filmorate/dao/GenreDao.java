package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.application.filmorate.exception.ObjectDoesNotExist;
import ru.application.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenreDao {
    private final JdbcTemplate jdbcTemplate;

    public List<Genre> get() {
        String sql = "SELECT id, name FROM genre";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    public Genre getById(int id) {
        String sql = "SELECT id, name FROM genre WHERE id = ?";
        try {
            Genre genre = jdbcTemplate.queryForObject(sql, (ResultSet rs, int rowNum) -> makeGenre(rs), id);
            if (genre != null) {
                log.info("Получен жанр: id = {}, название = {}", genre.getId(), genre.getName());
            }
            return genre;
        } catch (EmptyResultDataAccessException e) {
            String message = "Жанр с id = " + id + " не найден.";
            log.debug(message);
            throw new ObjectDoesNotExist(message);
        }
    }

    public static Genre makeGenre(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        return new Genre(id, name);
    }
}
