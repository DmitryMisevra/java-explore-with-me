package ru.yandex.practicum.mainservice.request.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.mainservice.request.dto.RequestDto;
import ru.yandex.practicum.mainservice.request.model.Request;

/**
 * RequestMapper для маппинга RequestDto
 */
@Component
public class RequestMapper {

    public RequestDto requestToRequestDto(Request request) {
        return RequestDto.builder()
                .created(request.getCreated())
                .event(request.getEvent())
                .id(request.getId())
                .requester(request.getRequester())
                .status(request.getStatus())
                .build();
    }
}
