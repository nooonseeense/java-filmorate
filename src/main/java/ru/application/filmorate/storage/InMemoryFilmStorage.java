package ru.application.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.application.filmorate.exception.FilmValidationException;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final static LocalDate THE_OLDEST_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;

    private int generatorId() {
        return filmId++;
    }

    public Film addFilm(Film filmFromRequest) {
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

    @Override
    public Film addLike(Integer id, Integer userId) {
        checkFilmInFilms(id);
        Film film = films.get(id);
        film.getNumOfLikes().add(userId);
        return film;
    }

    @Override
    public Film removeLike(Integer id, Integer userId) {
        checkFilmInFilms(id);
        Film film = films.get(id);
        if (!film.getNumOfLikes().contains(userId)) {
            String exceptionMessage = "Такого фильма нет в списке.";
            log.warn("Текст исключения: {}", exceptionMessage);
            throw new ObjectWasNotFoundException(exceptionMessage);
        }
        film.getNumOfLikes().remove(userId);
        return film;
    }

    @Override
    public List<Film> listTheTenMostPopularMoviesByTheNumberOfLikes(Integer count) {
        Comparator<Film> comparator = (f1, f2) -> f2.getNumOfLikes().size() - f1.getNumOfLikes().size();
        return films.values().stream()
                .sorted(comparator)
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Film> getFilmById(Integer filmId) {
        checkFilmInFilms(filmId);
        return Optional.ofNullable(films.get(filmId));
    }

    public Film updateFilm(Film filmFromRequest) {
        checkFilmInFilms(filmFromRequest.getId());
        Film film = validateFilm(filmFromRequest);
        films.remove(filmFromRequest.getId());
        films.put(film.getId(), film);
        log.info("Фильм был обновлен.");
        return film;
    }

    public List<Film> getAllFilms() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return List.copyOf(films.values());
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

    private void checkFilmInFilms(Integer id) {
        if (!films.containsKey(id)) {
            String exceptionMessage = "Такого фильма нет в списке.";
            log.warn("Текст исключения: {}", exceptionMessage);
            throw new ObjectWasNotFoundException(exceptionMessage);
        }
    }
}
