package ru.application.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.application.filmorate.impl.ReviewStorage;
import ru.application.filmorate.model.Review;

@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;

    public Review add(Review review) {
        return reviewStorage.add(review);
    }

    public Review getById(Integer reviewId) {
        return reviewStorage.getById(reviewId);
    }
}
