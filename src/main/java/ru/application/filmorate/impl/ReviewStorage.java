package ru.application.filmorate.impl;

import ru.application.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {

    Review add(Review review);

    Review getById(Integer reviewId);

    Review update(Review review);

    void delete(Integer reviewId);

    List<Review> getAll();

    List<Review> getAllByFilm(Integer filmId);

    void addLike(Integer reviewId, Integer userId);

    void addDislike(Integer reviewId, Integer userId);

    void deleteLike(Integer reviewId, Integer userId);

    void deleteDislike(Integer reviewId, Integer userId);

    Optional<Boolean> isRateLike(Integer reviewId, Integer userId);

    void changeUserRate(Integer reviewId, Integer userId, boolean rate);

    void recalculateUseful(Integer reviewId);
}
