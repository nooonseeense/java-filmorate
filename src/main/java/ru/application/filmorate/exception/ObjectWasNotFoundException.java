package ru.application.filmorate.exception;

/**
 * Класс собственного исключения при работе с объектом который не найден
 */
public class ObjectWasNotFoundException extends RuntimeException {
    public ObjectWasNotFoundException(String message) {
        super(message);
    }
}
