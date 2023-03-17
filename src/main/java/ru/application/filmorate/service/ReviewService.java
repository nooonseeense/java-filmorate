package ru.application.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.exception.ReviewValidationException;
import ru.application.filmorate.impl.ReviewStorage;
import ru.application.filmorate.model.Review;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final FilmService filmService;
    private final UserService userService;


    public Review add(Review review) {
        validateReview(review);
        return reviewStorage.add(review);
    }

    public Review update(Review review) {
        validateReview(review);
        return reviewStorage.update(review);
    }

    public Review getById(Integer reviewId) {
        try {
            return reviewStorage.getById(reviewId);
        } catch (EmptyResultDataAccessException e) {
            String message = String.format("Отзыв с идентификатором %d не найден.", reviewId);
            throw new ObjectWasNotFoundException(message);
        }
    }

    public void delete(Integer reviewId) {
        getById(reviewId);
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
        Optional<Boolean> isLike = reviewStorage.isRateLike(reviewId, userId);

        if (isLike.isPresent()) {
            if (isLike.get()) {
                throw new ReviewValidationException(String.format("Review with id %s already have like " +
                        "from user with id %s.", reviewId, userId));
            } else {
                reviewStorage.changeUserRate(reviewId, userId, true);
            }
        } else {
            reviewStorage.addLike(reviewId, userId);
        }
        reviewStorage.recalculateUseful(reviewId);
    }

    public void addDislike(Integer reviewId, Integer userId) {
        validateReviewAndUser(reviewId, userId);
        Optional<Boolean> isLike = reviewStorage.isRateLike(reviewId, userId);

        if (isLike.isPresent()) {
            if (!isLike.get()) {
                throw new ReviewValidationException(String.format("Review with id %s already have dislike " +
                        "from user with id %s.", reviewId, userId));
            } else {
                reviewStorage.changeUserRate(reviewId, userId, false);
            }
        } else {
            reviewStorage.addDislike(reviewId, userId);
        }
        reviewStorage.recalculateUseful(reviewId);
    }

    public void deleteLike(Integer reviewId, Integer userId) {
        validateReviewAndUser(reviewId, userId);
        Optional<Boolean> isLike = reviewStorage.isRateLike(reviewId, userId);

        if (isLike.isEmpty()) {
            throw new ReviewValidationException(String.format("Review with id %s don't have like " +
                    "from user with id %s.", reviewId, userId));
        } else if (isLike.get()) {
            reviewStorage.deleteLike(reviewId, userId);
            reviewStorage.recalculateUseful(reviewId);
        }
    }

    public void deleteDislike(Integer reviewId, Integer userId) {
        validateReviewAndUser(reviewId, userId);

        Optional<Boolean> isLike = reviewStorage.isRateLike(reviewId, userId);

        if (isLike.isEmpty()) {
            throw new ReviewValidationException(String.format("Review with id %s don't have dislike " +
                    "from user with id %s.", reviewId, userId));
        } else if (!isLike.get()) {
            reviewStorage.deleteDislike(reviewId, userId);
            reviewStorage.recalculateUseful(reviewId);
        }
    }

    private void validateReview(Review review) {
        filmService.getById(review.getFilmId());
        userService.getById(review.getUserId());
    }

    private void validateReviewAndUser(Integer reviewId, Integer userId) {
        getById(reviewId);
        userService.getById(userId);
    }
}
