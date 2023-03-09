package ru.application.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.storage.FilmStorage;

import java.util.List;

@Qualifier("InDbFilmStorage")
public class FilmDbStorageDao implements FilmStorage {

    @Override
    public List<Film> get() {
        return null;
    }

    @Override
    public Film getById(Integer filmId) {
        return null;
    }

    @Override
    public List<Film> getPopularMoviesByLikes(Integer count) {
        return null;
    }

    @Override
    public Film add(Film film) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public Film addLike(Integer id, Integer userId) {
        return null;
    }

    @Override
    public Film removeLike(Integer id, Integer userId) {
        return null;
    }
}
