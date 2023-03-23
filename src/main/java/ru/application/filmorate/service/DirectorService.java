package ru.application.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.application.filmorate.exception.ObjectDoesNotExist;
import ru.application.filmorate.model.Director;
import ru.application.filmorate.storage.DirectorStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class DirectorService {
    DirectorStorage directorStorage;

    /**
     * Метод получения списка режиссёров
     *
     * @return Список режиссёров
     */
    public List<Director> get() {
        return directorStorage.get();
    }

    /**
     * Метод получения объекта режиссёра
     *
     * @param id id режиссёра
     * @return Объект режиссёра
     */
    public Director get(int id) {
        return directorStorage.get(id)
                .orElseThrow(() -> new ObjectDoesNotExist("Режиссер с id =" + id + " не найден."));
    }

    /**
     * Метод создания режиссёра
     *
     * @param director Объект режиссёра
     * @return Объект режиссёра
     */
    public Director create(Director director) {
        return directorStorage.create(director)
                .orElseThrow(() -> new ObjectDoesNotExist("Произошла ошибка при создании режиссера."));
    }

    /**
     * Метод изменения объекта режиссёра
     *
     * @param director Объект режиссёра
     * @return Изменённый объект режиссёра
     */
    public Director update(Director director) {
        return directorStorage.update(director);
    }

    /**
     * Метод удаления режиссёра по ID
     *
     * @param id id режиссёра
     */
    public void delete(int id) {
        directorStorage.delete(id);
    }
}