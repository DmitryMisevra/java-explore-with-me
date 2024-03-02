package ru.yandex.practicum.mainservice.exceptions;

public class WrongInitiatorException extends RuntimeException {
    public WrongInitiatorException(String message) {
        super(message);
    }
}
