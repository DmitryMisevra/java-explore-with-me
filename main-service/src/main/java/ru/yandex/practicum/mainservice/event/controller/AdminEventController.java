package ru.yandex.practicum.mainservice.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.mainservice.event.dto.EventDto;
import ru.yandex.practicum.mainservice.event.dto.UpdatedEventDto;
import ru.yandex.practicum.mainservice.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final EventService eventService;

    /**
     * Получить список событий по условиям для админа
     *
     * @param userIds    список требуемых id cобытий
     * @param states     список требуемых статусов
     * @param categories список требуемых категорий
     * @param rangeStart начало временного промежутка
     * @param rangeEnd   конец временного промежутка
     * @param from       с какого индекса выдать список
     * @param size       размер списка
     * @return List<EventDto>
     */
    @GetMapping
    ResponseEntity<List<EventDto>> getEventListByAdminParameters(@RequestParam(required = false) Integer[] userIds,
                                                                 @RequestParam(required = false) String[] states,
                                                                 @RequestParam(required = false) Integer[] categories,
                                                                 @RequestParam(required = false) String rangeStart,
                                                                 @RequestParam(required = false) String rangeEnd,
                                                                 @RequestParam(defaultValue = "0") @Min(0) Long from,
                                                                 @RequestParam(defaultValue = "10") @Min(1) Long size) {
        List<EventDto> eventList = eventService.getEventListByAdminParameters(userIds, states, categories, rangeStart,
                rangeEnd, from, size);
        log.debug("Получен список событий {}", eventList);
        return ResponseEntity.ok(eventList);
    }

    /**
     * Обновить информацию о событии по id
     *
     * @param eventId         id события
     * @param updatedEventDto updatedEventDto
     * @return EventDto
     */
    @PatchMapping(path = "/{eventId}")
    ResponseEntity<EventDto> updateEventByAdmin(@PathVariable @Min(1) Long eventId,
                                                @Valid @RequestBody UpdatedEventDto updatedEventDto) {
        EventDto eventDto = eventService.updateEventByAdmin(eventId, updatedEventDto);
        log.debug("Обновлена информация о событии {}", eventDto);
        return ResponseEntity.ok(eventDto);
    }
}
