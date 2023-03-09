package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MpaDao {
    private final JdbcTemplate jdbcTemplate;

    public List<Mpa> get() {
        String sql = "SELECT id, name FROM mpa";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    public Mpa getById(int id) {
        String sql = "SELECT id, name FROM mpa WHERE id = ?";
        try {
            Mpa mpa = jdbcTemplate.queryForObject(sql, (ResultSet rs, int rowNum) -> makeMpa(rs), id);
            if (mpa != null) {
                log.info("Получен MPA-рейтинг: id = {}, название = {}", mpa.getId(), mpa.getName());
            }
            return mpa;
        } catch (ObjectWasNotFoundException e) {
            String message = String.format("MPA-рейтинг с id = %d не найден.", id);
            log.debug(message);
            throw new ObjectWasNotFoundException(message);
        }
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        return new Mpa(id, name);
    }
}
