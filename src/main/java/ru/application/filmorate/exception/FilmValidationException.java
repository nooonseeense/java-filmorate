package ru.application.filmorate.exception;

/**
 * Класс собственного исключения при работе с валидацией объекта фильм
 */
public class FilmValidationException extends RuntimeException {
    public FilmValidationException(String message) {
        super(message);
    }
}
