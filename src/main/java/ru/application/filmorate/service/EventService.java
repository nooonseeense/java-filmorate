package ru.application.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.application.filmorate.model.Event;
import ru.application.filmorate.storage.EventStorage;
import ru.application.filmorate.storage.UserStorage;
import ru.application.filmorate.storage.util.enumeration.EventType;
import ru.application.filmorate.storage.util.enumeration.OperationType;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor_ = {@Lazy})
public class EventService {
    private final EventStorage eventStorage;
    private final UserService userService;

    /**
     * Метод получения списка событий по ID пользователя
     *
     * @param userId id пользователя
     * @return Список событий
     */
    public List<Event> get(int userId) {
        userService.exists(userId);
        return eventStorage.get(userId);
    }

    /**
     * Метод создания события
     *
     * @param userId    id пользователя
     * @param eventType Тип события
     * @param operation Тип операции
     * @param entityId  Идентификатор сущности, с которой произошло событие
     */
    public void create(int userId, EventType eventType, OperationType operation, int entityId) {
        Event event = Event.builder()
                .timestamp(Timestamp.from(Instant.now()))
                .userId(userId)
                .eventType(eventType)
                .operation(operation)
                .entityId(entityId)
                .build();

        if (event != null) {
            eventStorage.add(event);
        }
    }
}
