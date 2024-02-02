package ru.yandex.practicum.mainservice.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.mainservice.event.dto.EventDto;
import ru.yandex.practicum.mainservice.event.dto.ShortEventDto;
import ru.yandex.practicum.mainservice.event.service.EventService;

import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final EventService eventService;

    /**
     * Получить список событий по параметрам для public-просмотра
     *
     * @param text          искомый текст в событии (по аннотации и описанию)
     * @param categories    список требуемых категорий
     * @param paid          бесплатное/платное событие
     * @param rangeStart    начало временного промежутка
     * @param rangeEnd      конец временного промежутка
     * @param onlyAvailable только события у которых не исчерпан лимит запросов на участие
     * @param sort          Вариант сортировки: по дате события или по количеству просмотров
     * @param from          индекс, с которого будет начинаться список
     * @param size          размер списка
     * @return List<ShortEventDto>
     */
    @GetMapping
    ResponseEntity<List<ShortEventDto>> getEventListByPublicParameters(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) Integer[] categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") @Min(0) Long from,
            @RequestParam(defaultValue = "10") @Min(1) Long size
    ) {
        List<ShortEventDto> eventDtoList = eventService.getEventListByPublicParameters(text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        log.debug("Получен список событий для public-просмотра {}", eventDtoList);
        return ResponseEntity.ok(eventDtoList);
    }

    /**
     * Получить полную информацию о событии по его id
     *
     * @param eventId id события
     * @return EventDto
     */
    @GetMapping(path = "/{eventId}")
    ResponseEntity<EventDto> getPublishedEventInfoById(@PathVariable @Min(1) Long eventId) {
        EventDto eventDto = eventService.getPublishedEventInfoById(eventId);
        log.debug("Получена полная информация о событии {}", eventDto);
        return ResponseEntity.ok(eventDto);
    }
}
