package ru.application.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.application.filmorate.enumeration.EventType;
import ru.application.filmorate.enumeration.OperationType;

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
    OperationType operation;
    int entityId;
}
