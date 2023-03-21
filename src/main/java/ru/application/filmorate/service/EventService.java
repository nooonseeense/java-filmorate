package ru.application.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.application.filmorate.model.Event;
import ru.application.filmorate.storage.event.EventStorage;
import ru.application.filmorate.storage.user.UserStorage;
import ru.application.filmorate.util.enumeration.EventType;
import ru.application.filmorate.util.enumeration.Operation;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class EventService {
    private final EventStorage eventStorage;
    private final UserStorage userStorage;

    /**
     * Метод получения списка событий по ID пользователя
     *
     * @param userId id пользователя
     * @return Список событий
     */
    public List<Event> getEventByUserId(int userId) {
        userStorage.getById(userId);
        return eventStorage.getEventByUserId(userId);
    }

    /**
     * Метод создания события
     *
     * @param userId    id пользователя
     * @param eventType Тип события
     * @param operation Тип операции
     * @param entityId  Идентификатор сущности, с которой произошло событие
     */
    public void createEvent(int userId, EventType eventType, Operation operation, int entityId) {
        Event event = Event.builder()
                .timestamp(Timestamp.from(Instant.now()))
                .userId(userId)
                .eventType(eventType)
                .operation(operation)
                .entityId(entityId)
                .build();

        if (event != null) {
            eventStorage.addEvent(event);
        }
    }
}
