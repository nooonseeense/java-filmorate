package ru.application.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.application.filmorate.exception.ObjectDoesNotExist;
import ru.application.filmorate.model.Genre;
import ru.application.filmorate.storage.GenreStorage;
import ru.application.filmorate.storage.util.Mapper;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GenreDao implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> get() {
        return jdbcTemplate.query("SELECT ID, NAME FROM GENRE", Mapper::genreMapper);
    }

    @Override
    public Genre get(int id) {
        try {
            Genre genre = jdbcTemplate.queryForObject("SELECT ID, NAME FROM GENRE WHERE ID = ?",
                    Mapper::genreMapper, id
            );

            if (genre != null) {
                log.info("Получен жанр: ID = {}, название = {}", genre.getId(), genre.getName());
            }
            return genre;
        } catch (EmptyResultDataAccessException e) {
            String message = "Жанр с ID = " + id + " не найден.";
            log.debug("get(int id): Жанр с ID = {} не найден.", id);
            throw new ObjectDoesNotExist(message);
        }
    }
}