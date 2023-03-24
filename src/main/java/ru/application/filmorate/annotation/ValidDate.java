package ru.application.filmorate.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LocalDateFilmValidation.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDate {
    int year();

    int m();

    int day();

    String message() default "{ValidDate}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
