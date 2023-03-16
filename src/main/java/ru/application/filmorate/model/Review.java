package ru.application.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@AllArgsConstructor
public class Review {

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
