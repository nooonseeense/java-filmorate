package ru.application.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;
import ru.application.filmorate.model.Event;
import ru.application.filmorate.storage.EventStorage;
import ru.application.filmorate.storage.util.Mapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventDao implements EventStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Event> get(int userId) {
        return jdbcTemplate.query("SELECT * FROM EVENT WHERE USER_ID = ?", Mapper::eventMapper, userId);
    }

    @Override
    public void add(Event event) {
        jdbcTemplate.update(
                    "INSERT INTO EVENT(TIME_STAMP, USER_ID, EVENT_TYPE, EVENT_OPERATION, ENTITY_ID) " +
                        "VALUES (?, ?, ?, ?, ?)",
                event.getTimestamp(),
                event.getUserId(),
                event.getEventType().toString(),
                event.getOperation().toString(),
                event.getEntityId()
        );
    }
}