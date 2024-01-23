package ru.yandex.practicum.statsclient.exceptions;

public class DateTimeValidationException extends RuntimeException {
    public DateTimeValidationException(String message) {
        super(message);
    }
}
