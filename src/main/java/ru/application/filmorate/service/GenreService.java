package ru.application.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.application.filmorate.dao.GenreDao;
import ru.application.filmorate.model.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDao genreDao;

    public List<Genre> get() {
        return genreDao.get();
    }

    public Genre getById(int id) {
        return genreDao.getById(id);
    }
}
