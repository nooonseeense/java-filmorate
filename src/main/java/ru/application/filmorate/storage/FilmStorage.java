package ru.application.filmorate.storage;

import ru.application.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    void addALikeToAMovie();

    void removeALikeFromAMovie();

    List<Film> outputMovieByLikes();
}
