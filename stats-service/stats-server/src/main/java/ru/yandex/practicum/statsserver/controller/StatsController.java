package ru.yandex.practicum.statsserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.statsdto.hit.CreatedHitDto;
import ru.yandex.practicum.statsdto.hit.HitDto;
import ru.yandex.practicum.statsdto.hit.StatsDto;
import ru.yandex.practicum.statsserver.service.StatsService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    /**
     * Добавление события
     *
     * @param createdHitDto createdHitDto
     * @return HitDto
     */

    @PostMapping("/hit")
    public ResponseEntity<HitDto> createHit(@Valid @RequestBody CreatedHitDto createdHitDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(statsService.createHit(createdHitDto));
    }

    /**
     * Запрос статистики событий по заданным параметрам
     *
     * @param start  начало запрашиваемого периода
     * @param end    конец запрашиваемого периода
     * @param uris   требуемые uri
     * @param unique проверка на уникальность ip
     * @return List<StatsDto>
     */

    @GetMapping("/stats")
    public ResponseEntity<List<StatsDto>> getStats(@RequestParam String start,
                                                   @RequestParam String end,
                                                   @RequestParam(required = false) String[] uris,
                                                   @RequestParam(defaultValue = "false") Boolean unique) {
        return ResponseEntity.ok(statsService.getStats(start, end, uris, unique));
    }
}
