package ru.yandex.practicum.statsdto.hit;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * HitDto передается в http-ответе при создании события в StatsController
 */

@Data
@Builder
public class HitDto {

    private Long id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}
