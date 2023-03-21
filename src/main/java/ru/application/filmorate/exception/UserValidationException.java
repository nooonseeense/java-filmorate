package ru.application.filmorate.exception;

/**
 * Класс собственного исключения при работе с валидацией объекта пользователь
 */
public class UserValidationException extends RuntimeException {
    public UserValidationException(String message) {
        super(message);
    }
}
