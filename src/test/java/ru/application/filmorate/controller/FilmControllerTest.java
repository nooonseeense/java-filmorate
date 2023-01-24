package ru.application.filmorate.controller;

import org.junit.Test;
import ru.application.filmorate.exception.FilmValidationException;
import ru.application.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest extends FilmController {
    FilmController filmController = new FilmController();

    @Test
    public void shouldBeReturnExceptionWhenAddingAMovie() {
        Film film = new Film(
                "Kill Bill",
                "A pregnant hitman named Black Mamba is shot during her marriage by a man named Bill.",
                LocalDate.of(2003, 12, 20),
                113);
        assertEquals(film, filmController.addFilm(film), "Фильм не был добавлен");

        Film film2 = new Film(
                "Kill Bill",
                "A pregnant hitman named Black Mamba is shot during her marriage by a man named Bill.",
                LocalDate.of(2003, 12, 20),
                113);
        film2.setId(film.getId());

        final FilmValidationException ex = assertThrows(FilmValidationException.class, () -> filmController.addFilm(film2));
        assertEquals("Фильм с таким названием " + film2.getName() + " уже добавлен.", ex.getMessage());
    }

    @Test
    public void updateFilmTest() {
        Film film = new Film(
                "Kill Bill",
                "A pregnant hitman named Black Mamba is shot during her marriage by a man named Bill.",
                LocalDate.of(2003, 12, 20),
                113);
        assertEquals(film, filmController.addFilm(film), "Фильм не был добавлен");

        Film film2 = new Film(
                "Kill Bill PART TWO",
                "A pregnant hitman named Black Mamba is shot during her marriage by a man named Bill.",
                LocalDate.of(2003, 12, 20),
                113);
        film2.setId(film.getId());

        assertEquals(film.getName() + " PART TWO", filmController.updateFilm(film2).getName(), "Фильм не был обновлен.");
    }

    @Test
    public void shouldReturnAnExceptionIfStringName() {
        Film film = new Film(
                "",
                "A pregnant hitman named Black Mamba is shot during her marriage by a man named Bill.",
                LocalDate.of(2003, 12, 20),
                113);
        final FilmValidationException ex = assertThrows(FilmValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Название не может быть пустым.", ex.getMessage());
    }

    @Test
    public void shouldReturnAnExceptionIfNullName() {
        Film film = new Film(
                null,
                "A pregnant hitman named Black Mamba is shot during her marriage by a man named Bill.",
                LocalDate.of(2003, 12, 20),
                113);
        final FilmValidationException ex = assertThrows(FilmValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Название не может быть пустым.", ex.getMessage());
    }

    @Test
    public void shouldReturnAnExceptionIfMore200Char() {
        Film film = new Film(
                "Kill Bill",
                "A pregnant hitman named Black Mamba is shot during her marriage by a man named Bill.\n" +
                        "A pregnant hitman named Black Mamba is shot during her marriage by a man named Bill.\n" +
                        "A pregnant hitman named Black M",
                LocalDate.of(2003, 12, 20),
                113);
        final FilmValidationException ex = assertThrows(FilmValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Максимальная длина описания — 200 символов.", ex.getMessage());
    }

    @Test
    public void mustReturnAnExceptionIfTheReleaseDateIsBefore28December1895Year() {
        Film film = new Film(
                "Kill Bill",
                "A pregnant hitman named Black Mamba is shot during her marriage by a man named Bill.",
                LocalDate.of(1895, Month.DECEMBER, 27),
                113);
        final FilmValidationException ex = assertThrows(FilmValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Дата релиза не может быть раньше " + LocalDate.of(1895, 12, 28), ex.getMessage());
    }

    @Test
    public void shouldReturnAnExceptionIfTheMovieDurationIsNegative() {
        Film film = new Film(
                "Kill Bill",
                "A pregnant hitman named Black Mamba is shot during her marriage by a man named Bill.",
                LocalDate.of(1895, Month.DECEMBER, 29),
                0);
        final FilmValidationException ex = assertThrows(FilmValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Продолжительность фильма должна быть положительной.", ex.getMessage());
    }

    @Test
    public void getFilmsTest() {
        Film film = new Film(
                "Kill Bill",
                "A pregnant hitman named Black Mamba is shot during her marriage by a man named Bill.",
                LocalDate.of(1895, Month.DECEMBER, 29),
                120);
        filmController.addFilm(film);
        Film film2 = new Film(
                "Kill Bill Two",
                "A pregnant hitman named Black Mamba is shot during her marriage by a man named Bill.",
                LocalDate.of(1920, Month.JANUARY, 15),
                150);
        filmController.addFilm(film2);
        int result = filmController.getFilms().size();
        assertEquals(2, result, "Количество фильмов не совпадает со списком.");
    }
}