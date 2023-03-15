package ru.application.filmorate.impl;

import ru.application.filmorate.model.Review;

public interface ReviewStorage {

    Review add(Review review);

    Review getById(Integer reviewId);
}
