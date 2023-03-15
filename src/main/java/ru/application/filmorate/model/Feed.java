package ru.application.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.application.filmorate.enums.EventType;
import ru.application.filmorate.enums.Operation;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class Feed {
    int eventId;
    Timestamp timestamp;
    int userId;
    EventType eventType;
    Operation operation;
    int entityId;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("timestamp", timestamp);
        values.put("userId", userId);
        values.put("eventType", eventType);
        values.put("operation", operation);
        values.put("entityId", entityId);
        return values;
    }
}
