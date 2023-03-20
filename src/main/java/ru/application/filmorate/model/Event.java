package ru.application.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.application.filmorate.util.enumeration.EventType;
import ru.application.filmorate.util.enumeration.Operation;

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
    Operation operation;
    int entityId;
}
