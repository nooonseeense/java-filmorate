package ru.application.filmorate.exception;

/**
 * Класс собственного исключения при работе с валидацией объекта "отзыв"
 */
public class ReviewValidationException extends RuntimeException {
    public ReviewValidationException(String message) {
        super(message);
    }
}
