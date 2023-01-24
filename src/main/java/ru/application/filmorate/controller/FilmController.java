package ru.application.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.application.filmorate.exception.FilmValidationException;
import ru.application.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/films")
@RestController
@Slf4j
public class FilmController {
    public final static int MAX_DESCRIPTION_LENGTH = 200;
    private final static LocalDate THE_OLDEST_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;

    private int generatorId() {
        return filmId++;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film filmFromRequest) {
        log.info("Получен запрос 'POST /films'");
        if (films.containsValue(filmFromRequest)) {
            String exceptionMessage = "Фильм с таким названием " + filmFromRequest.getName() + " уже добавлен.";
            log.warn("Ошибка при добавлении фильма. Текст исключения: {}", exceptionMessage);
            throw new FilmValidationException(exceptionMessage);
        }
        Film film = validateFilm(filmFromRequest);
        film.setId(generatorId());
        films.put(film.getId(), film);
        log.info("Фильм был добавлен.");
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film filmFromRequest) {
        log.info("Получен запрос 'PUT /films'");
        if (!films.containsKey(filmFromRequest.getId())) {
            String exceptionMessage = "Такого фильма нет в списке.";
            log.warn("Ошибка при обновлении фильма. Текст исключения: {}", exceptionMessage);
            throw new FilmValidationException(exceptionMessage);
        }
        Film film = validateFilm(filmFromRequest);
        films.remove(filmFromRequest.getId());
        films.put(film.getId(), film);
        log.info("Фильм был обновлен.");
        return film;
    }

    private Film validateFilm(Film film) {
        String exceptionMessage;
        if (film.getReleaseDate().isBefore(THE_OLDEST_RELEASE_DATE)) {
            exceptionMessage = "Дата релиза не может быть раньше " + THE_OLDEST_RELEASE_DATE;
            log.warn("Ошибка при валидации фильма. Текст исключения: {}", exceptionMessage);
            throw new FilmValidationException(exceptionMessage);
        }
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получен запрос 'GET /films'");
        log.debug("Текущее количество фильмов: {}", films.size());
        return List.copyOf(films.values());
    }
}
