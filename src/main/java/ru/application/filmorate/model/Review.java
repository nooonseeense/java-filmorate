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
    private int id;
    @NotBlank
    private String content;
    @NotNull
    private Boolean isPositive;
    @PositiveOrZero
    @NotNull
    private Integer userId;
    @PositiveOrZero
    @NotNull
    private Integer filmId;
    private int useful;
}
