package ru.application.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

import static ru.application.filmorate.controller.FilmController.MAX_DESCRIPTION_LENGTH;

@Data
public class Film {
    private int id;
    @NotBlank(message = "Название не может быть пустым.")
    private final String name;
    @Size(max = 200, message = "Максимальная длина описания — " + MAX_DESCRIPTION_LENGTH + " символов.")
    private final String description;
    @NotNull(message = "Необходимо заполнить дату релиза.")
    private final LocalDate releaseDate;
    @Min(value = 1, message = "Продолжительность фильма должна быть положительной.")
    private final int duration;
}
