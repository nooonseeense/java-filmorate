package ru.application.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.application.filmorate.exception.ObjectDoesNotExist;
import ru.application.filmorate.model.Director;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.storage.DirectorStorage;
import ru.application.filmorate.storage.util.Mapper;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DirectorDao implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public List<Director> get() {
        return jdbcTemplate.query("SELECT * FROM DIRECTOR", Mapper::directorMapper);
    }

    @Override
    public Optional<Director> get(Integer id) {
        try {
            Director director = jdbcTemplate.queryForObject("SELECT * " +
                    "FROM DIRECTOR WHERE ID = ?", Mapper::directorMapper, id);

            if (director != null) {
                log.info("Найден режиссер: c ID = {} именем = {}", director.getId(), director.getName());
            }

            return Optional.ofNullable(director);
        } catch (EmptyResultDataAccessException e) {
            String message = String.format("Режиссер с ID = %d не найден.", id);
            log.debug("get(Integer id): режиссер с ID = {} не найден.", id);
            throw new ObjectDoesNotExist(message);
        }
    }

    @Override
    public Optional<Director> create(Director director) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO DIRECTOR (NAME) VALUES (?)",
                            new String[]{"id"}
                    );
            preparedStatement.setString(1, director.getName());
            return preparedStatement;
        }, keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        director.setId(id);
        return Optional.of(director);
    }

    @Override
    public Director update(Director director) {
        int newRows = jdbcTemplate.update("UPDATE DIRECTOR SET NAME = ? WHERE ID = ?",
                director.getName(), director.getId()
        );
        if (newRows == 0) {
            String message = "Директор " + director + " не найден.";
            log.debug("update(Director director): Режиссёр {} не найден.", director);
            throw new ObjectDoesNotExist(message);
        }
        return director;
    }

    @Override
    public void delete(int id) {
        int result = jdbcTemplate.update("DELETE FROM DIRECTOR WHERE ID = ?", id);
        if (result == 0) {
            String message = "Режиссер с ID = " + id + " не найден.";
            log.debug("delete(int id): Режиссер с ID = {} не найден.", id);
            throw new ObjectDoesNotExist(message);
        }
    }

    @Override
    public void set(List<Film> films) {
        if (films != null) {
            List<Integer> filmsId = films.stream().map(Film::getId).collect(Collectors.toList());
            Map<Integer, Film> filmsMap = films.stream().collect(Collectors.toMap(Film::getId, Function.identity()));
            SqlParameterSource source = new MapSqlParameterSource("filmsId", filmsId);
            SqlRowSet set = namedJdbcTemplate.queryForRowSet(
                    "SELECT FILM_ID, d.* " +
                    "FROM FILM_DIRECTOR AS fd " +
                    "JOIN DIRECTOR AS d ON d.ID = fd.DIRECTOR_ID " +
                    "WHERE fd.FILM_ID IN (:filmsId)",
                    source
            );
            while (set.next()) {
                int filmId = set.getInt("film_id");
                int directorId = set.getInt("id");
                String directorName = set.getString("name");
                filmsMap.get(filmId).getDirectors().add(new Director(directorId, directorName));
            }
        }
    }
}
