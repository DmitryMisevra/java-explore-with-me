package ru.yandex.practicum.statsserver.exceptions;

public class DateTimeValidationException extends RuntimeException {
    public DateTimeValidationException(String message) {
        super(message);
    }
}
