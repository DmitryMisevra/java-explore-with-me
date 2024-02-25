package ru.yandex.practicum.mainservice.request.service;

import ru.yandex.practicum.mainservice.request.dto.RequestDto;
import ru.yandex.practicum.mainservice.request.dto.RequestStatusAggregateDto;
import ru.yandex.practicum.mainservice.request.dto.RequestStatusUpdateDto;

import java.util.List;

public interface RequestService {

    List<RequestDto> getUserRequestList(Long userId);

    RequestDto createRequest(Long userId, Long eventId);

    RequestDto cancelRequest(Long userId, Long requestId);

    RequestStatusAggregateDto updateRequestStatusByInitiator(Long userId, Long eventId,
                                                             RequestStatusUpdateDto requestStatusUpdateDto);

    List<RequestDto> getRequestListForUserEvent(Long userId, Long eventId);


}
