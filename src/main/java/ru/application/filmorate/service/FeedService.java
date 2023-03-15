package ru.application.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.application.filmorate.impl.FeedStorage;
import ru.application.filmorate.impl.UserStorage;
import ru.application.filmorate.model.Feed;

import java.util.List;

@Service
@AllArgsConstructor
public class FeedService {
    private final FeedStorage feedStorage;
    private final UserStorage userStorage;
    public List<Feed> getFeedByUserId(int userId){
        userStorage.getById(userId);
        return feedStorage.getFeedByUserId(userId);
    }
}
