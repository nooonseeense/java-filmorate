package ru.application.filmorate.model;

import lombok.Builder;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import ru.application.filmorate.annotation.ValidDate;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

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
    @NotNull
    private Mpa mpa;
    @JsonIgnore
    private int rating;
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();
}