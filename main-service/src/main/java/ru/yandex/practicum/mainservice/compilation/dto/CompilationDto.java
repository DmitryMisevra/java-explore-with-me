package ru.yandex.practicum.mainservice.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.mainservice.event.dto.ShortEventDto;

import java.util.List;


/**
 * CompilationDto передается в http-ответе
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {

    private List<ShortEventDto> events;
    private Long id;
    private Boolean pinned;
    private String title;
}
