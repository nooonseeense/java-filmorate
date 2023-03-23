package ru.application.filmorate.exception;

/**
 * Класс собственного исключения при работе с объектом который не существует или не найден
 */
public class ObjectDoesNotExist extends RuntimeException {
    public ObjectDoesNotExist(String message) {
        super(message);
    }
}
