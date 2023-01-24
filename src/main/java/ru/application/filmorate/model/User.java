package ru.application.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;
    @Email
    @NotNull
    @NotBlank
    private final String email;
    private final String login;
    @Setter
    private String name;
    @Past
    private final LocalDate birthday;
}
