package ru.application.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.application.filmorate.storage.util.enumeration.EventType;
import ru.application.filmorate.storage.util.enumeration.OperationType;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
public class Event {
    int eventId;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    Timestamp timestamp;
    int userId;
    EventType eventType;
    OperationType operationType;
    int entityId;
}
