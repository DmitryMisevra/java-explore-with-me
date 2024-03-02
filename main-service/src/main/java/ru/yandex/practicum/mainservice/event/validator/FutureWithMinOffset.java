package ru.yandex.practicum.mainservice.event.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Валидатор времени события
 */
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = FutureWithMinOffsetValidator.class)
public @interface FutureWithMinOffset {

    String message() default "Время события должно быть минимум на 2 часа позже текущего времени";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
