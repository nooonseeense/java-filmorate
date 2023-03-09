package ru.application.filmorate.model;

import jdk.jshell.Snippet;
import lombok.Builder;
import lombok.Data;
import ru.application.filmorate.annotation.ValidDate;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
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
    private int duration;
    private Mpa mpa;
    private int rating;
    private int numOfLikes;
    private Set<Integer> likes = new HashSet<>();
    private List<Genre> genres = new ArrayList<>();
}