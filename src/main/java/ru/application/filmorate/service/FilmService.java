package ru.application.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.application.filmorate.storage.FilmStorage;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Lazy FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public FilmStorage getFilmStorage() {
        return filmStorage;
    }
}
