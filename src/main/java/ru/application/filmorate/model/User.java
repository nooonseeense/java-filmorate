package ru.application.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @.")
    @NotBlank
    private final String email;
    @NotBlank(message = "Логин не может быть пустым или содержать пробелы.")
    private final String login;
    private String name;
    @Past(message = "Дата рождения не может быть в будущем.")
    private final LocalDate birthday;
}
