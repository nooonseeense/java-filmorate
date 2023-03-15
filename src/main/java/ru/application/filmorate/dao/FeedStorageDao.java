package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
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
        return jdbcTemplate.query(sql, Mapper::feedMapper);
    }

    @Override
    public void addFeed(Feed feed) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FEEDS")
                .usingGeneratedKeyColumns("EVENT_ID");

        simpleJdbcInsert.execute(feed.toMap());
    }
}
