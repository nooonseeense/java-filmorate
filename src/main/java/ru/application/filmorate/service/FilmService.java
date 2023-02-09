package ru.application.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.application.filmorate.exception.FilmValidationException;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FilmService implements FilmStorage {
    private final FilmStorage filmStorage;
    private final static LocalDate THE_OLDEST_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film filmFromRequest) {
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

    public Film updateFilm(Film filmFromRequest) {
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

    public List<Film> getFilms() {
        log.info("Получен запрос 'GET /films'");
        log.debug("Текущее количество фильмов: {}", films.size());
        return List.copyOf(films.values());
    }

    private int generatorId() {
        return filmId++;
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

    @Override
    public void addALikeToAMovie() {

    }

    @Override
    public void removeALikeFromAMovie() {

    }

    @Override
    public List<Film> outputMovieByLikes() {
        return null;
    }
}
