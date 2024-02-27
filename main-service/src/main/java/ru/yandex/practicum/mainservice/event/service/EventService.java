package ru.yandex.practicum.mainservice.event.service;

import ru.yandex.practicum.mainservice.event.dto.CreatedEventDto;
import ru.yandex.practicum.mainservice.event.dto.EventDto;
import ru.yandex.practicum.mainservice.event.dto.ShortEventDto;
import ru.yandex.practicum.mainservice.event.dto.UpdatedEventDto;
import ru.yandex.practicum.mainservice.event.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    List<ShortEventDto> getEventListByInitiatorId(Long userId, Long from, Long size);

    EventDto createEvent(Long userId, CreatedEventDto createdEventDto);

    EventDto getFullEventInfoByInitiator(Long userId, Long eventId);

    EventDto updateEventByInitiator(Long userId, Long eventId, UpdatedEventDto updatedEventDto);

    List<EventDto> getEventListByAdminParameters(Integer[] userIds, String[] states, Integer[] categories,
                                                 String rangeStart,
                                                 String rangeEnd, Long from, Long size);

    EventDto updateEventByAdmin(Long eventId, UpdatedEventDto updatedEventDto);

    List<ShortEventDto> getEventListByPublicParameters(String text, Integer[] categories, Boolean paid,
                                                       String rangeStart, String rangeEnd,
                                                       Boolean onlyAvailable, String sort, Long from, Long size,
                                                       HttpServletRequest request);

    EventDto getPublishedEventInfoById(Long eventId, HttpServletRequest request);

    Event setViewsAndConfirmedRequests(Event event);
}
