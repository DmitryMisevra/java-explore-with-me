package ru.yandex.practicum.mainservice.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.mainservice.request.model.RequestStatus;

import java.time.LocalDateTime;

/**
 * RequestDto передается и возвращается в http-запросах и ответах
 */
@Data
@Builder
public class RequestDto {

    private final LocalDateTime created;
    private final Long event;
    private final Long id;
    private final Long requester;
    private final RequestStatus status;
}
