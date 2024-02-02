package ru.yandex.practicum.mainservice.event.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.mainservice.event.dto.CreatedEventDto;
import ru.yandex.practicum.mainservice.event.dto.EventDto;
import ru.yandex.practicum.mainservice.event.dto.RequestStatusAggregateDto;
import ru.yandex.practicum.mainservice.event.dto.RequestStatusUpdateDto;
import ru.yandex.practicum.mainservice.event.dto.ShortEventDto;
import ru.yandex.practicum.mainservice.event.dto.UpdatedEventDto;
import ru.yandex.practicum.mainservice.event.mapper.EventMapper;
import ru.yandex.practicum.mainservice.event.mapper.LocationMapper;
import ru.yandex.practicum.mainservice.event.repository.EventRepository;
import ru.yandex.practicum.mainservice.event.repository.LocationRepository;
import ru.yandex.practicum.mainservice.request.dto.RequestDto;
import ru.yandex.practicum.mainservice.request.repository.RequestRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;
    private final RequestRepository requestRepository;

    @PersistenceContext
    private final EntityManager entityManager;


    @Override
    public List<ShortEventDto> getEventListByInitiatorId(Long userId, Long from, Long size) {
        return null;
    }

    @Override
    @Transactional
    public EventDto createEvent(Long userId, CreatedEventDto createdEventDto) {
        return null;
    }

    @Override
    public EventDto getFullEventInfoByInitiator(Long userId, Long eventId) {
        return null;
    }

    @Override
    @Transactional
    public EventDto updateEventByInitiator(Long userId, Long eventId, UpdatedEventDto updatedEventDto) {
        return null;
    }

    @Override
    public List<RequestDto> getRequestListForUserEvent(Long userId, Long eventId) {
        return null;
    }

    @Override
    @Transactional
    public RequestStatusAggregateDto updateRequestStatusByInitiator(Long userId, Long eventId,
                                                                    RequestStatusUpdateDto requestStatusUpdateDto) {
        return null;
    }

    @Override
    @Transactional
    public List<EventDto> getEventListByAdminParameters(Integer[] userIds, String[] states, Integer[] categories,
                                                        String rangeStart, String rangeEnd, Long from, Long size) {
        return null;
    }

    @Override
    @Transactional
    public EventDto updateEventByAdmin(Long eventId, UpdatedEventDto updatedEventDto) {
        return null;
    }

    @Override
    public List<ShortEventDto> getEventListByPublicParameters(String text, Integer[] categories, Boolean paid,
                                                              String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                              String sort, Long from, Long size) {
        return null;
    }

    @Override
    public EventDto getPublishedEventInfoById(Long eventId) {
        return null;
    }
}
