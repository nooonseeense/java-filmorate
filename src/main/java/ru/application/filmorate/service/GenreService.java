package ru.application.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.application.filmorate.model.Genre;
import ru.application.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    /**
     * Метод получения списка всех жанров
     *
     * @return Список всех жанров
     */
    public List<Genre> get() {
        return genreStorage.get();
    }

    /**
     * Метод получения жанра по ID
     *
     * @param id id жанра
     * @return Объект жанра
     */
    public Genre getById(int id) {
        return genreStorage.getById(id);
    }
}
