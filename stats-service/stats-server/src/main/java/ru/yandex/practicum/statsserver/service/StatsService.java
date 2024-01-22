package ru.yandex.practicum.statsserver.service;

import ru.yandex.practicum.statsdto.hit.CreatedHitDto;
import ru.yandex.practicum.statsdto.hit.HitDto;
import ru.yandex.practicum.statsdto.hit.StatsDto;

import java.util.List;

public interface StatsService {

    HitDto createHit(CreatedHitDto createdHitDto);

    List<StatsDto> getStats(String start, String end, String[] uris, Boolean unique);
}
