package ru.yandex.practicum.statsserver.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.statsdto.hit.CreatedHitDto;
import ru.yandex.practicum.statsdto.hit.HitDto;
import ru.yandex.practicum.statsserver.model.Hit;

/**
 * HitMapper для маппинга Dto
 */

@Component
public class HitMapper {

    public Hit createdHitDtoToHit(CreatedHitDto createdHitDto) {
        return Hit.builder()
                .app(createdHitDto.getApp())
                .uri(createdHitDto.getUri())
                .ip(createdHitDto.getIp())
                .timestamp(createdHitDto.getTimestamp())
                .build();
    }

    public HitDto hitToHitDto(Hit hit) {
        return HitDto.builder()
                .id(hit.getId())
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp())
                .build();
    }
}
