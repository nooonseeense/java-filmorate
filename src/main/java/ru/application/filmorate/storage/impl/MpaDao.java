package ru.application.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;
import ru.application.filmorate.exception.ObjectDoesNotExist;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.model.Mpa;
import ru.application.filmorate.storage.util.Mapper;
import ru.application.filmorate.storage.MpaStorage;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MpaDao implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> get() {
        return jdbcTemplate.query("SELECT ID, NAME FROM MPA", Mapper::mpaMapper);
    }

    @Override
    public Mpa get(int id) {
        try {
            Mpa mpa = jdbcTemplate.queryForObject("SELECT ID, NAME FROM MPA WHERE ID = ?", Mapper::mpaMapper, id);
            if (mpa != null) {
                log.info("Получен MPA-рейтинг: id = {}, название = {}", mpa.getId(), mpa.getName());
            }
            return mpa;
        } catch (EmptyResultDataAccessException e) {
            String message = String.format("MPA-рейтинг с id = %d не найден.", id);
            log.debug("get(int id): MPA-рейтинг с id = {} не найден.", id);
            throw new ObjectDoesNotExist(message);
        }
    }

    @Override
    public void set(Film film) {
        Mpa mpa = jdbcTemplate.queryForObject("SELECT ID, NAME FROM MPA WHERE ID = ?",
                Mapper::mpaMapper, film.getMpa().getId());
        if (mpa != null) {
            film.setMpa(mpa);
        }
    }
}
