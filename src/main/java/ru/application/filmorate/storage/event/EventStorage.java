package ru.application.filmorate.storage.event;

import ru.application.filmorate.model.Event;

import java.util.List;

public interface EventStorage {

    List<Event> getEventByUserId(int userId);

    void addEvent(Event event);
}
