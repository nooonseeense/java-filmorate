package ru.application.filmorate.model;

import lombok.Data;
import ru.application.filmorate.annotation.ValidDate;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    @PositiveOrZero
    private int id;
    @NotBlank(message = "Название не может быть пустым.")
    private final String name;
    @NotNull
    @Size(max = 200, message = "Максимальная длина описания — 200 символов.")
    private final String description;
    @NotNull(message = "Необходимо заполнить дату релиза.")
    @ValidDate(year = 1895, m = 12, day = 28)
    private final LocalDate releaseDate;
    @Min(value = 1, message = "Продолжительность фильма должна быть положительной.")
    private final int duration;
    private final Mpa mpa;
    private final int rating;
    private final int likes;
    private final Set<Integer> numOfLikes = new HashSet<>();
    private final Set<Genre> genres = new HashSet<>();
}