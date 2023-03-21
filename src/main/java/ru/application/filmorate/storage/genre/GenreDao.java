package ru.application.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.application.filmorate.exception.ObjectDoesNotExist;
import ru.application.filmorate.model.Genre;
import ru.application.filmorate.util.Mapper;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenreDao implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> get() {
        String sql = "SELECT id, name FROM genre";
        return jdbcTemplate.query(sql, Mapper::genreMapper);
    }

    @Override
    public Genre getById(int id) {
        String sql = "SELECT id, name FROM genre WHERE id = ?";
        try {
            Genre genre = jdbcTemplate.queryForObject(sql, Mapper::genreMapper, id);
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
}
