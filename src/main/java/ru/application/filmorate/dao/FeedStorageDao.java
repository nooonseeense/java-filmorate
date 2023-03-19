package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.application.filmorate.impl.FeedStorage;
import ru.application.filmorate.model.Feed;
import ru.application.filmorate.util.Mapper;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FeedStorageDao implements FeedStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Feed> getFeedByUserId(int userId) {
        String sql = "SELECT * FROM FEED WHERE USER_ID = ?";
        return jdbcTemplate.query(sql, Mapper::feedMapper, userId);
    }

    @Override
    public void addFeed(Feed feed) {
        String sql = "INSERT INTO FEED (TIMESTAMP,USER_ID,EVENT_TYPE,OPERATION,ENTITY_ID) " +
                "VALUES (?,?,?,?,?)";
        jdbcTemplate.update(sql,
                feed.getTimestamp(),
                feed.getUserId(),
                feed.getEventType().toString(),
                feed.getOperation().toString(),
                feed.getEntityId());
    }
}
