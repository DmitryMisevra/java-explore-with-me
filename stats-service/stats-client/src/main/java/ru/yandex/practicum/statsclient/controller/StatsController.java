package ru.yandex.practicum.statsclient.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.statsclient.client.StatsClient;
import ru.yandex.practicum.statsdto.hit.CreatedHitDto;
import ru.yandex.practicum.statsdto.hit.HitDto;
import ru.yandex.practicum.statsdto.hit.StatsDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsClient statsClient;

    /**
     * Добавление события
     *
     * @param createdHitDto createdHitDto
     * @return HitDto
     */

    @PostMapping("/hit")
    public ResponseEntity<HitDto> createHit(@Valid @RequestBody CreatedHitDto createdHitDto) {
        return statsClient.createHit(createdHitDto);
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
        return statsClient.getStats(start, end, uris, unique);
    }
}
