package ru.application.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    @PositiveOrZero
    private int id;
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @.")
    @NotBlank
    private final String email;
    @NotBlank(message = "Логин не может быть пустым или состоять из пробелов.")
    @Pattern(regexp = "([^\\s]+)", message = "Логин не может содержать пробелы.")
    private final String login;
    private String name;
    @NotNull
    @PastOrPresent(message = "Дата рождения не может быть в будущем.")
    private final LocalDate birthday;
    private final Set<Integer> friendsId = new HashSet<>();
}