package ru.yandex.practicum.statsdto.hit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * StatsDto передается в http-ответе для сбора статистики о количестве событий для каждого app
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsDto {

    private String app;
    private String uri;
    public Long hits;
}
