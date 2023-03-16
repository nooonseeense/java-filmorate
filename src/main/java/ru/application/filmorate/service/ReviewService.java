package ru.application.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.application.filmorate.enums.EventType;
import ru.application.filmorate.enums.Operation;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.impl.ReviewStorage;
import ru.application.filmorate.model.Review;
import ru.application.filmorate.model.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final FeedService feedService;
    private final FilmService filmService;
    private final UserService userService;


    public Review add(Review review) {
        validateReview(review);
        Review addedReview = reviewStorage.add(review);
        feedService.createFeed(addedReview.getUserId(), EventType.REVIEW, Operation.ADD, addedReview.getId());
        return addedReview;
            }

    public Review update(Review review) {
        validateReview(review);
        return reviewStorage.update(review);
    }

    public Review getById(Integer reviewId) {
        if (reviewNotExists(reviewId)) {
            log.debug("Отзыв с id {} не найден.", reviewId);
            throw new ObjectWasNotFoundException(String.format("Отзыв с id %d не найден.", reviewId));
        }
        return reviewStorage.getById(reviewId);
    }

    public void delete(Integer reviewId) {
        if (reviewNotExists(reviewId)) {
            log.debug("Отзыв с id {} не найден.", reviewId);
            throw new ObjectWasNotFoundException(String.format("Отзыв с id %d не найден.", reviewId));
        }
        reviewStorage.delete(reviewId);
    }

    public List<Review> getAllByFilm(Integer filmId, Integer count) {
        List<Review> reviews;

        if (filmId == null) {
            reviews = reviewStorage.getAll();
        } else {
            reviews = reviewStorage.getAllByFilm(filmId);
        }

        return reviews.stream()
                .sorted(Comparator.comparing(Review::getUseful).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public void addLike(Integer reviewId, Integer userId) {
        validateReviewAndUser(reviewId, userId);
        reviewStorage.addLike(reviewId, userId);
    }

    public void addDislike(Integer reviewId, Integer userId) {
        validateReviewAndUser(reviewId, userId);
        reviewStorage.addDislike(reviewId, userId);
    }

    public void deleteLike(Integer reviewId, Integer userId) {
        validateReviewAndUser(reviewId, userId);
        reviewStorage.deleteLike(reviewId, userId);
    }

    public void deleteDislike(Integer reviewId, Integer userId) {
        validateReviewAndUser(reviewId, userId);
        reviewStorage.deleteDislike(reviewId, userId);
    }

    private void validateReview(Review review) {
        filmService.getById(review.getFilmId());
        userService.getById(review.getUserId());
    }

    private void validateReviewAndUser(Integer reviewId, Integer userId) {
        if (reviewNotExists(reviewId)) {
            log.debug("Отзыв с id {} не найден.", reviewId);
            throw new ObjectWasNotFoundException(String.format("Отзыв с id %d не найден.", reviewId));
        }
        if (userNotExists(userId)) {
            log.debug("Пользователь с id {} не найден.", userId);
            throw new ObjectWasNotFoundException(String.format("Пользователь с id %d не найден.", userId));
        }

    }

    public boolean reviewNotExists(int reviewId) {
        List<Review> reviews = reviewStorage.getAll();
        for (Review review : reviews) {
            if (review.getReviewId() == reviewId) {
                return false;
            }
        }
        return true;
    }

    public boolean userNotExists(int userId) {
        List<User> users = userService.get();
        for (User user : users) {
            if (user.getId() == userId) {
                return false;
            }
        }
        return true;
    }
}
