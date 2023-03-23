package ru.application.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Map;

/**
 * Класс обработки ошибок
 */
@Slf4j
@RestControllerAdvice(basePackages = "ru.application.filmorate.controller")
public class ErrorHandler {
    public static final String ERROR = "error";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUserValidateEx(final UserValidationException e) {
        log.warn("Ошибка при валидации пользователя.");
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleFilmValidateEx(final FilmValidationException e) {
        log.warn("Ошибка при валидации фильма.");
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleReviewValidateEx(final ReviewValidationException e) {
        log.warn("Ошибка при валидации отзыва.");
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler({NullPointerException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleNullOrIllegalArgumentEx(final RuntimeException e) {
        log.warn("Сервер столкнулся с неожиданной ошибкой, которая помешала выполнить запрос.");
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgumentEx(final RuntimeException e) {
        log.warn("Некорректное значение.");
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleConstraintViolationEx(final RuntimeException e) {
        log.warn("Ошибка в параметре запроса.");
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleThrowableEx(final Throwable e) {
        log.warn("Сервер столкнулся с неожиданной ошибкой, которая помешала выполнить запрос.");
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValidEx(final RuntimeException e) {
        log.warn("Ошибка при обработке запроса.");
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler({ObjectDoesNotExist.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleObjectDoesNotExistException(final ObjectDoesNotExist e) {
        log.warn("Запрашиваемый объект не найден.");
        return Map.of(ERROR, e.getMessage());
    }
}