package ru.application.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "ru.application.filmorate.controller")
public class ErrorHandler {

    private static final String ERROR = "error";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUserValidateEx(final UserValidationException e) {
        log.warn("Текст исключения: Ошибка при валидации пользователя.");
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleFilmValidateEx(final FilmValidationException e) {
        log.warn("Текст исключения: Ошибка при валидации фильма.");
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleReviewValidateEx(final ReviewValidationException e) {
        log.warn("Текст исключения: Ошибка при валидации отзыва.");
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundEx(final ObjectWasNotFoundException e) {
        log.warn("Текст исключения: Объект не был найден.");
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleNullOrIllegalArgumentEx(final RuntimeException e) {
        log.warn("Текст исключения: Сервер столкнулся с неожиданной ошибкой, которая помешала выполнить запрос.");
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleConstraintViolationEx(final RuntimeException e) {
        log.warn("Текст исключения: Ошибка в параметре запроса.");
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleThrowableEx(final Throwable e) {
        log.warn("Текст исключения: Сервер столкнулся с неожиданной ошибкой, которая помешала выполнить запрос.");
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValidEx(final RuntimeException e) {
        log.warn("Текст исключения: Ошибка при обработке запроса.");
        return Map.of(ERROR, e.getMessage());
    }

    @ExceptionHandler({ObjectDoesNotExist.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleObjectDoesNotExistException(final ObjectDoesNotExist e) {
        log.warn("Текст исключения: Запрашиваемый объект не найден.");
        return Map.of(ERROR, e.getMessage());
    }
}