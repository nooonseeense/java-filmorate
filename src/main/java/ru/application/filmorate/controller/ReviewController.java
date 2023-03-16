package ru.application.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.model.Review;
import ru.application.filmorate.service.ReviewService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

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

}
