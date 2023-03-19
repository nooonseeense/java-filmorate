package ru.application.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.application.filmorate.enums.EventType;
import ru.application.filmorate.enums.Operation;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
public class Feed {
    int eventId;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    Timestamp timestamp;
    int userId;
    EventType eventType;
    Operation operation;
    int entityId;
}
