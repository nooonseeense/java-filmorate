package ru.application.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.application.filmorate.enums.EventType;
import ru.application.filmorate.enums.Operation;
import ru.application.filmorate.impl.ReviewStorage;
import ru.application.filmorate.model.Review;

@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final FeedService feedService;

    public Review add(Review review) {
        Review addedReview = reviewStorage.add(review);
        feedService.createFeed(addedReview.getUserId(), EventType.REVIEW, Operation.ADD, addedReview.getId());
        return addedReview;
    }

    public Review getById(Integer reviewId) {
        return reviewStorage.getById(reviewId);
    }
}
