package ru.yandex.practicum.mainservice.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.mainservice.event.dto.CreatedEventDto;
import ru.yandex.practicum.mainservice.event.dto.EventDto;
import ru.yandex.practicum.mainservice.event.dto.RequestStatusAggregateDto;
import ru.yandex.practicum.mainservice.event.dto.RequestStatusUpdateDto;
import ru.yandex.practicum.mainservice.event.dto.ShortEventDto;
import ru.yandex.practicum.mainservice.event.dto.UpdatedEventDto;
import ru.yandex.practicum.mainservice.event.service.EventService;
import ru.yandex.practicum.mainservice.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {

    private final EventService eventService;

    /**
     * Получить список событий, созданных пользователем
     *
     * @param userId id пользователя
     * @param from   индекс, с которого начинается список
     * @param size   размер списка
     * @return List<ShortEventDto>
     */
    @GetMapping
    ResponseEntity<List<ShortEventDto>> getEventListByInitiatorId(
            @PathVariable @Min(1) Long userId,
            @RequestParam(defaultValue = "0") @Min(0) Long from,
            @RequestParam(defaultValue = "10") @Min(1) Long size
    ) {
        List<ShortEventDto> eventList = eventService.getEventListByInitiatorId(userId, from, size);
        log.debug("Получен список событий {}", eventList);
        return ResponseEntity.ok(eventList);
    }

    /**
     * Создать событие
     *
     * @param userId          id пользователя
     * @param createdEventDto createdEventDto
     * @return EventDto
     */
    @PostMapping
    ResponseEntity<EventDto> createEvent(@PathVariable @Min(1) Long userId,
                                         @Valid @RequestBody CreatedEventDto createdEventDto) {
        EventDto eventDto = eventService.createEvent(userId, createdEventDto);
        log.debug("Создано событие {}", eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventDto);
    }

    /**
     * Получить полную информацию о событии, созданного пользователем
     *
     * @param userId  id пользователя
     * @param eventId id события
     * @return EventDto
     */
    @GetMapping(path = "/{eventId}")
    ResponseEntity<EventDto> getFullEventInfoByInitiator(@PathVariable @Min(1) Long userId,
                                                         @PathVariable @Min(1) Long eventId) {
        EventDto eventDto = eventService.getFullEventInfoByInitiator(userId, eventId);
        log.debug("Получена информация о событии {}", eventDto);
        return ResponseEntity.ok(eventDto);
    }

    /**
     * Обновить информацию о событии, созданном пользователем
     *
     * @param userId          id пользователя
     * @param eventId         id события
     * @param updatedEventDto updatedEventDto
     * @return EventDto
     */
    @PatchMapping(path = "/{eventId}")
    ResponseEntity<EventDto> updateEventByInitiator(@PathVariable @Min(1) Long userId,
                                                    @PathVariable @Min(1) Long eventId,
                                                    @Valid @RequestBody UpdatedEventDto updatedEventDto) {
        EventDto eventDto = eventService.updateEventByInitiator(userId, eventId, updatedEventDto);
        log.debug("Обновлена информация о событии {}", eventDto);
        return ResponseEntity.ok(eventDto);
    }

    /**
     * Получить список запросов для события, созданного пользователем
     *
     * @param userId  id пользователя
     * @param eventId id события
     * @return List<RequestDto>
     */
    @GetMapping(path = "/{eventId}/requests")
    ResponseEntity<List<RequestDto>> getRequestListForUserEvent(@PathVariable @Min(1) Long userId,
                                                                @PathVariable @Min(1) Long eventId) {
        List<RequestDto> requestDtoList = eventService.getRequestListForUserEvent(userId, eventId);
        log.debug("Получен список запросов для запрашиваемого события {}", requestDtoList);
        return ResponseEntity.ok(requestDtoList);
    }

    /**
     * Обновить статусы запросов на участие
     *
     * @param userId                 id пользователя
     * @param eventId                id события
     * @param requestStatusUpdateDto requestStatusUpdateDto
     * @return RequestStatusAggregateDto
     */
    @PatchMapping(path = "/{eventId}/requests")
    ResponseEntity<RequestStatusAggregateDto> updateRequestStatusByInitiator(@PathVariable @Min(1) Long userId,
                                                                             @PathVariable @Min(1) Long eventId,
                                                                             @Valid @RequestBody RequestStatusUpdateDto
                                                                                     requestStatusUpdateDto) {
        RequestStatusAggregateDto requestStatusAggregateDto = eventService.updateRequestStatusByInitiator(userId,
                eventId, requestStatusUpdateDto);
        log.debug("Обновлены статусы запросов на участие: {}", requestStatusAggregateDto);
        return ResponseEntity.ok(requestStatusAggregateDto);
    }
}
