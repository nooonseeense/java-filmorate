package ru.application.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.application.filmorate.exception.FilmValidationException;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;

    @Override
    public List<Film> get() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return List.copyOf(films.values());
    }

    @Override
    public Film getById(Integer filmId) {
        checkFilmInFilms(filmId);
        return films.get(filmId);
    }

    @Override
    public List<Film> getPopularMoviesByLikes(Integer count) {
        Comparator<Film> comparator = (f1, f2) -> f2.getNumOfLikes().size() - f1.getNumOfLikes().size();
        return films.values().stream()
                .sorted(comparator)
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film add(Film film) {
        if (films.containsValue(film)) {
            String exceptionMessage = "Фильм с таким названием " + film.getName() + " уже добавлен.";
            log.warn("Ошибка при добавлении фильма. Текст исключения: {}", exceptionMessage);
            throw new FilmValidationException(exceptionMessage);
        }
        film.setId(generatorId());
        films.put(film.getId(), film);
        log.info("Фильм был добавлен.");
        return film;
    }

    @Override
    public Film update(Film film) {
        checkFilmInFilms(film.getId());
        films.put(film.getId(), film);
        log.info("Фильм был обновлен.");
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

    private void checkFilmInFilms(Integer id) {
        if (!films.containsKey(id)) {
            String exceptionMessage = "Такого фильма нет в списке.";
            log.warn("Текст исключения: {}", exceptionMessage);
            throw new ObjectWasNotFoundException(exceptionMessage);
        }
    }

    private int generatorId() {
        return filmId++;
    }
}