package ru.application.filmorate.storage.event;

import ru.application.filmorate.model.Event;

import java.util.List;

public interface EventStorage {
    /**
     * Метод получения списка событий по ID пользователя
     *
     * @param userId id пользователя
     * @return Список событий
     */
    List<Event> getEventByUserId(int userId);

    /**
     * Метод создания события
     *
     * @param event объект события
     */
    void addEvent(Event event);
}
