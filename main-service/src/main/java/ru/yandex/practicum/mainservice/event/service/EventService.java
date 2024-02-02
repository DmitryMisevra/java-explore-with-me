package ru.yandex.practicum.mainservice.event.service;

import ru.yandex.practicum.mainservice.event.dto.CreatedEventDto;
import ru.yandex.practicum.mainservice.event.dto.EventDto;
import ru.yandex.practicum.mainservice.event.dto.RequestStatusAggregateDto;
import ru.yandex.practicum.mainservice.event.dto.RequestStatusUpdateDto;
import ru.yandex.practicum.mainservice.event.dto.ShortEventDto;
import ru.yandex.practicum.mainservice.event.dto.UpdatedEventDto;
import ru.yandex.practicum.mainservice.request.dto.RequestDto;

import java.util.List;

public interface EventService {

    // private
    List<ShortEventDto> getEventListByInitiatorId(Long userId, Long from, Long size);

    EventDto createEvent(Long userId, CreatedEventDto createdEventDto);

    EventDto getFullEventInfoByInitiator(Long userId, Long eventId);

    EventDto updateEventByInitiator(Long userId, Long eventId, UpdatedEventDto updatedEventDto);

    List<RequestDto> getRequestListForUserEvent(Long userId, Long eventId);

    RequestStatusAggregateDto updateRequestStatusByInitiator(Long userId,
                                                             Long eventId,
                                                             RequestStatusUpdateDto requestStatusUpdateDto);


    // admin
    List<EventDto> getEventListByAdminParameters(Integer[] userIds, String[] states, Integer[] categories,
                                                 String rangeStart,
                                                 String rangeEnd, Long from, Long size);

    EventDto updateEventByAdmin(Long eventId, UpdatedEventDto updatedEventDto);

    // public - здесь вызов каждого эндпойнта отправлять в статистику
    List<ShortEventDto> getEventListByPublicParameters(String text, Integer[] categories, Boolean paid,
                                                       String rangeStart, String rangeEnd,
                                                       Boolean onlyAvailable, String sort, Long from, Long size);

    EventDto getPublishedEventInfoById(Long eventId);
}
