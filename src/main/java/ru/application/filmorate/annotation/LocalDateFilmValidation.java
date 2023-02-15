package ru.application.filmorate.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class LocalDateFilmValidation implements ConstraintValidator<ValidDate, LocalDate> {
    private static LocalDate THE_OLDEST_RELEASE_DATE;

    @Override
    public void initialize(ValidDate constraintAnnotation) {
        int year = constraintAnnotation.year();
        int month = constraintAnnotation.m();
        int day = constraintAnnotation.day();
        THE_OLDEST_RELEASE_DATE = LocalDate.of(year, month, day);
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext cxt) {
        return localDate.isAfter(THE_OLDEST_RELEASE_DATE);
    }
}