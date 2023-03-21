package ru.application.filmorate.storage.director;

import ru.application.filmorate.model.Director;
import ru.application.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface DirectorStorage {
     List<Director> get();

     Optional<Director> get(int id);

     Optional<Director> create(Director director);

     Director update(Director director);

     void delete(int id);

     void setDirectors(List<Film> films);
}
