package ru.application.filmorate.storage.director;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.model.Director;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.util.Mapper;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DirectorDao implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public List<Director> get() {
        return jdbcTemplate.query("SELECT * FROM DIRECTOR", Mapper::directorMapper);
    }

    @Override
    public Optional<Director> get(int id) {
        String sql = "SELECT * FROM DIRECTOR WHERE ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        if (!rs.next()) {
            return Optional.empty();
        }
        Director director = jdbcTemplate.queryForObject(sql, Mapper::directorMapper, id);
        return Optional.ofNullable(director);
    }

    @Override
    public Optional<Director> create(Director director) {
        String sqlQuery = "INSERT INTO DIRECTOR (NAME) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, new String[]{"id"});
            preparedStatement.setString(1, director.getName());
            return preparedStatement;
        }, keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        director.setId(id);
        return Optional.of(director);
    }

    @Override
    public Director update(Director director) {
        String sql = "UPDATE DIRECTOR SET NAME = ? WHERE ID = ?";
        int newRows = jdbcTemplate.update(sql, director.getName(), director.getId());
        if (newRows == 0) {
            String message = "Директор " + director + " не найден.";
            log.debug(message);
            throw new ObjectWasNotFoundException(message);
        }
        return director;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM DIRECTOR WHERE ID = ?";
        int result = jdbcTemplate.update(sql, id);
        if (result == 0) {
            String message = "Режиссер с id = " + id + " не найден.";
            log.debug(message);
            throw new ObjectWasNotFoundException(message);
        }
    }

    @Override
    public void setDirectors(List<Film> films) {
        if (films != null) {
            String sqlDirectors =
                    "SELECT FILM_ID, d.* " +
                    "FROM FILM_DIRECTOR " +
                    "JOIN DIRECTOR AS d ON d.ID = FILM_DIRECTOR.DIRECTOR_ID " +
                    "WHERE FILM_DIRECTOR.FILM_ID IN (:filmsId)";

            List<Integer> filmsId = films.stream().map(Film::getId).collect(Collectors.toList());
            Map<Integer, Film> filmsMap = films.stream().collect(Collectors.toMap(Film::getId, Function.identity()));

            SqlParameterSource source = new MapSqlParameterSource("filmsId", filmsId);
            SqlRowSet set = namedJdbcTemplate.queryForRowSet(sqlDirectors, source);

            while (set.next()) {
                int filmId = set.getInt("film_id");
                int directorId = set.getInt("id");
                String directorName = set.getString("name");
                filmsMap.get(filmId).getDirectors().add(new Director(directorId, directorName));
            }
        }
    }
}
