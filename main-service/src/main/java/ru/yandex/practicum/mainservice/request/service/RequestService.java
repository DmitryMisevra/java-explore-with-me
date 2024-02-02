package ru.yandex.practicum.mainservice.request.service;

import ru.yandex.practicum.mainservice.request.dto.RequestDto;

import java.util.List;

public interface RequestService {

    List<RequestDto> getUserRequestList(Long userId);

    RequestDto createRequest(Long userId, Long eventId, RequestDto requestDto);

    RequestDto cancelRequest(Long userId, Long requestId);
}
