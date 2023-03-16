package ru.application.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
public class Review {

    @PositiveOrZero
    private int reviewId;
    @NotBlank
    private String content;
    @NotNull
    private Boolean isPositive;
    @NotNull
    private Integer userId;
    @NotNull
    private Integer filmId;
    private int useful;
}
