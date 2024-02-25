package ru.yandex.practicum.mainservice.event.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class FutureWithMinOffsetValidator implements ConstraintValidator<FutureWithMinOffset, LocalDateTime> {
    @Override
    public void initialize(FutureWithMinOffset constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        LocalDateTime now = LocalDateTime.now();
        return value.isAfter(now) && ChronoUnit.HOURS.between(now, value) >= 2;
    }
}
