package ru.application.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.application.filmorate.model.Genre;
import ru.application.filmorate.impl.GenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public List<Genre> get() {
        return genreStorage.get();
    }

    public Genre getById(int id) {
        return genreStorage.getById(id);
    }
}
