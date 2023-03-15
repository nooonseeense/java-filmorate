package ru.application.filmorate.impl;

import ru.application.filmorate.model.Feed;

import java.util.List;

public interface FeedStorage {

    List<Feed> getFeedByUserId(int userId);
    void addFeed(Feed feed);
}
