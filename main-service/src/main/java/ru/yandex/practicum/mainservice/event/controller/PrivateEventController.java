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

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{eventId}/events")
@RequiredArgsConstructor
public class PrivateEventController {

    @GetMapping
    ResponseEntity<List<EventDto>> getEventListByUserId(@PathVariable Long eventId,
                                                        @RequestParam(defaultValue = "0") Long from,
                                                        @RequestParam(defaultValue = "10") Long size) {
        return null;
    }
}
