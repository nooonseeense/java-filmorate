package ru.application.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.application.filmorate.model.Review;
import ru.application.filmorate.service.ReviewService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public Review add(@Valid @RequestBody Review review) {
        return reviewService.add(review);
    }

    @GetMapping("{reviewId}")
    public Review getById(@PositiveOrZero @PathVariable Integer reviewId) {
        return reviewService.getById(reviewId);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        return reviewService.update(review);
    }

    @DeleteMapping("{reviewId}")
    public void delete(@PositiveOrZero @PathVariable Integer reviewId) {
        reviewService.delete(reviewId);
    }

    @GetMapping()
    public List<Review> getAllByFilm(@RequestParam(name = "filmId", required = false) Integer filmId,
                                     @RequestParam(name = "count", required = false, defaultValue = "10") Integer count
    ) {
        return reviewService.getAllByFilm(filmId, count);
    }

    @PutMapping("{reviewId}/like/{userId}")
    public void addLike(@PositiveOrZero @PathVariable Integer reviewId,
                        @PositiveOrZero @PathVariable Integer userId
    ) {
        reviewService.addLike(reviewId, userId);
    }

    @PutMapping("{reviewId}/dislike/{userId}")
    public void addDislike(@PositiveOrZero @PathVariable Integer reviewId,
                           @PositiveOrZero @PathVariable Integer userId
    ) {
        reviewService.addDislike(reviewId, userId);
    }

    @DeleteMapping("{reviewId}/like/{userId}")
    public void deleteLike(@PositiveOrZero @PathVariable Integer reviewId,
                           @PositiveOrZero @PathVariable Integer userId
    ) {
        reviewService.deleteLike(reviewId, userId);
    }

    @DeleteMapping("{reviewId}/dislike/{userId}")
    public void deleteDislike(@PositiveOrZero @PathVariable Integer reviewId,
                              @PositiveOrZero @PathVariable Integer userId
    ) {
        reviewService.deleteDislike(reviewId, userId);
    }
}
