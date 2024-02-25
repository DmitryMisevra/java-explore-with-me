package ru.yandex.practicum.mainservice.error;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.yandex.practicum.mainservice.event.dto.CreatedEventDto;
import ru.yandex.practicum.mainservice.event.validator.FutureWithMinOffset;
import ru.yandex.practicum.mainservice.exceptions.EmailAlreadyExistsException;
import ru.yandex.practicum.mainservice.exceptions.InvalidStateException;
import ru.yandex.practicum.mainservice.exceptions.NotFoundException;
import ru.yandex.practicum.mainservice.exceptions.WrongInitiatorException;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        HttpStatus responseStatus = HttpStatus.BAD_REQUEST;
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            Field field = ReflectionUtils.findField(CreatedEventDto.class, fieldError.getField());
            if (field != null && field.isAnnotationPresent(FutureWithMinOffset.class)) {
                responseStatus = HttpStatus.CONFLICT;
                break;
            }
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(responseStatus.name())
                .errorType(ex.getClass().getSimpleName())
                .reason("Incorrectly made request.")
                .message(errorMessage)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        return new ResponseEntity<>(errorResponse, headers, responseStatus);
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class,
            MissingRequestHeaderException.class, WrongInitiatorException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationException(final RuntimeException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .errorType(e.getClass().getSimpleName())
                .reason("Incorrectly made request.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    @ExceptionHandler({NotFoundException.class, EmptyResultDataAccessException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final RuntimeException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.name())
                .errorType(e.getClass().getSimpleName())
                .reason("Entity not found")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    @ExceptionHandler({EmailAlreadyExistsException.class, DataIntegrityViolationException.class,
            InvalidStateException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final RuntimeException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.name())
                .errorType(e.getClass().getSimpleName())
                .reason("Integrity constraint has been violated.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherErrors(final Throwable e) {
        return ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .errorType(e.getClass().getSimpleName())
                .reason("Incorrectly made request.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<ErrorResponse> handleHttpStatusCodeException(HttpStatusCodeException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(e.getStatusCode().toString())
                .errorType(e.getClass().getSimpleName())
                .reason("Incorrectly made request for stats.")
                .message(e.getResponseBodyAsString())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        return new ResponseEntity<>(errorResponse, e.getStatusCode());
    }
}
