package ru.application.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.application.filmorate.enums.EventType;
import ru.application.filmorate.enums.Operation;
import ru.application.filmorate.impl.FeedStorage;
import ru.application.filmorate.impl.UserStorage;
import ru.application.filmorate.model.Feed;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class FeedService {
    private final FeedStorage feedStorage;
    private final UserStorage userStorage;

    public List<Feed> getFeedByUserId(int userId) {
        userStorage.getById(userId);
        return feedStorage.getFeedByUserId(userId);
    }

    public void createFeed(int userId, EventType eventType, Operation operation, int entityId) {
        Feed feed = Feed.builder()
                .timestamp(Timestamp.from(Instant.now()))
                .userId(userId)
                .eventType(eventType)
                .operation(operation)
                .entityId(entityId)
                .build();

        if (feed != null) {
            feedStorage.addFeed(feed);
        }
    }
}
