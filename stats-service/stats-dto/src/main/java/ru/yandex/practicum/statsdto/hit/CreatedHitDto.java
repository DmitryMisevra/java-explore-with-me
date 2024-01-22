package ru.yandex.practicum.statsdto.hit;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * CreatedHitDto передается при создании события в базе статистики
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatedHitDto {

    @NotBlank(message = "не указано имя приложения")
    private String app;

    @NotBlank(message = "не указан имя uri")
    private String uri;

    @NotBlank(message = "не указан ip пользователя")
    private String ip;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
