package ru.yandex.practicum.mainservice.exceptions;

public class DateTimeValidationException extends RuntimeException {
    public DateTimeValidationException(String message) {
        super(message);
    }
}