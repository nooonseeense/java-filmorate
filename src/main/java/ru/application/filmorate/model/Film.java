package ru.application.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.application.filmorate.annotation.ValidDate;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
@Builder
@AllArgsConstructor
public class Film {

    private int id;
    @NotBlank(message = "Название не может быть пустым.")
    private final String name;
    @NotNull
    @Size(max = 200, message = "Максимальная длина описания — 200 символов.")
    private final String description;
    @NotNull(message = "Необходимо заполнить дату релиза.")
    @ValidDate(year = 1895, m = 12, day = 28)
    private final LocalDate releaseDate;
    @NotNull
    private Mpa mpa;
    @Min(value = 1, message = "Продолжительность фильма должна быть положительной.")
    private int duration;
    private LinkedHashSet<Genre> genres;
}