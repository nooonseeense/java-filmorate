package ru.application.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.storage.director.DirectorStorage;
import ru.application.filmorate.model.Director;

import java.util.List;

@Service
@AllArgsConstructor
public class DirectorService {
    DirectorStorage directorStorage;

    public List<Director> get() {
        return directorStorage.get();
    }

    public Director get(int id) {
        return directorStorage.get(id)
                .orElseThrow(() -> new ObjectWasNotFoundException("Режиссер с id =" + id + " не найден."));
    }

    public Director create(Director director) {
        return directorStorage.create(director)
                .orElseThrow(() -> new ObjectWasNotFoundException("Произошла ошибка при создании режиссера."));
    }

    public Director update(Director director) {
        return directorStorage.update(director);
    }

    public void delete(int id) {
        directorStorage.delete(id);
    }
}