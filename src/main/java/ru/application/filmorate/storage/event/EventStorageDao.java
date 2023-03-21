package ru.application.filmorate.storage.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.application.filmorate.model.Event;
import ru.application.filmorate.util.Mapper;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventStorageDao implements EventStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Event> getEventByUserId(int userId) {
        String sql = "SELECT * FROM EVENT WHERE USER_ID = ?";
        return jdbcTemplate.query(sql, Mapper::eventMapper, userId);
    }

    @Override
    public void addEvent(Event event) {
        String sql = "INSERT INTO EVENT (TIME_STAMP,USER_ID,EVENT_TYPE,EVENT_OPERATION,ENTITY_ID) " +
                "VALUES (?,?,?,?,?)";
        jdbcTemplate.update(sql,
                event.getTimestamp(),
                event.getUserId(),
                event.getEventType().toString(),
                event.getOperation().toString(),
                event.getEntityId());
    }
}
