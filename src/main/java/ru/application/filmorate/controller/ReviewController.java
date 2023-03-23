package ru.application.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.application.filmorate.model.Review;
import ru.application.filmorate.service.ReviewService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public Review add(@Valid @RequestBody Review review) {
        log.info("Запрос POST: add(Review review) на создание отзыва.");
        return reviewService.add(review);
    }

    @GetMapping("{reviewId}")
    public Review get(@PositiveOrZero @PathVariable Integer reviewId) {
        log.info("Запрос GET: get(Integer reviewId) на получение отзыва по ID = {}.", reviewId);
        return reviewService.get(reviewId);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        log.info("Запрос PUT: update(Review review) на изменение отзыва.");
        return reviewService.update(review);
    }

    @DeleteMapping("{reviewId}")
    public void delete(@PositiveOrZero @PathVariable Integer reviewId) {
        log.info("Запрос DELETE: delete(Integer reviewId) на удаление отзыва по ID = {}.", reviewId);
        reviewService.delete(reviewId);
    }

    @GetMapping()
    public List<Review> get(@RequestParam(name = "filmId", required = false) Integer filmId,
                            @RequestParam(name = "count", required = false, defaultValue = "10") Integer count
    ) {
        log.info("Запрос GET: get(Integer filmId, Integer count) на получение списка отзывов по фильму" +
                " с параметрами: FILM ID = {}, COUNT = {}.", filmId, count);
        return reviewService.get(filmId, count);
    }

    @PutMapping("{reviewId}/like/{userId}")
    public void addLike(@PositiveOrZero @PathVariable Integer reviewId,
                        @PositiveOrZero @PathVariable Integer userId
    ) {
        log.info("Запрос PUT: addLike(Integer reviewId, Integer userId) на добавление лайка отзыву = {} от " +
                "пользователя = {}.", reviewId, userId);
        reviewService.addLike(reviewId, userId);
    }

    @PutMapping("{reviewId}/dislike/{userId}")
    public void addDislike(@PositiveOrZero @PathVariable Integer reviewId,
                           @PositiveOrZero @PathVariable Integer userId
    ) {
        log.info("Запрос PUT: addDislike(Integer reviewId, Integer userId) на добавление дизлайка отзыву = {} от " +
                "пользователя = {}.", reviewId, userId);
        reviewService.addDislike(reviewId, userId);
    }

    @DeleteMapping("{reviewId}/like/{userId}")
    public void deleteLike(@PositiveOrZero @PathVariable Integer reviewId,
                           @PositiveOrZero @PathVariable Integer userId
    ) {
        log.info("Запрос DELETE: deleteLike(Integer reviewId, Integer userId) на удаление лайка у отзыва = {} от " +
                "пользователя = {}.", reviewId, userId);
        reviewService.deleteLike(reviewId, userId);
    }

    @DeleteMapping("{reviewId}/dislike/{userId}")
    public void deleteDislike(@PositiveOrZero @PathVariable Integer reviewId,
                              @PositiveOrZero @PathVariable Integer userId
    ) {
        log.info("Запрос DELETE: deleteDislike(Integer reviewId, Integer userId) на удаление дизлайка у отзыва = {} от " +
                        "пользователя = {}.", reviewId, userId
        );
        reviewService.deleteDislike(reviewId, userId);
    }
}
