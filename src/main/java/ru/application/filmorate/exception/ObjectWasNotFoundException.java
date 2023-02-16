package ru.application.filmorate.exception;

public class ObjectWasNotFoundException extends RuntimeException {
    public ObjectWasNotFoundException(String message) {
        super(message);
    }
}
