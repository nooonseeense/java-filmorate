package ru.application.filmorate.exception;

public class ObjectDoesNotExist extends RuntimeException {
    public ObjectDoesNotExist(String message) {
        super(message);
    }
}
