package ru.application.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.application.filmorate.exception.ObjectDoesNotExist;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.model.Mpa;
import ru.application.filmorate.util.Mapper;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MpaDao implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> get() {
        String sql = "SELECT ID, NAME FROM MPA";
        return jdbcTemplate.query(sql, Mapper::mpaMapper);
    }

    @Override
    public Mpa getById(int id) {
        String sql = "SELECT ID, NAME FROM MPA WHERE ID = ?";
        try {
            Mpa mpa = jdbcTemplate.queryForObject(sql, Mapper::mpaMapper, id);
            if (mpa != null) {
                log.info("Получен MPA-рейтинг: id = {}, название = {}", mpa.getId(), mpa.getName());
            }
            return mpa;
        } catch (EmptyResultDataAccessException e) {
            String message = String.format("MPA-рейтинг с id = %d не найден.", id);
            log.debug(message);
            throw new ObjectDoesNotExist(message);
        }
    }

    @Override
    public void setMpa(Film film) {
        Mpa mpa = jdbcTemplate.queryForObject("SELECT ID, NAME FROM MPA WHERE ID = ?",
                Mapper::mpaMapper, film.getMpa().getId());
        if (mpa != null) {
            film.setMpa(mpa);
        }
    }
}
