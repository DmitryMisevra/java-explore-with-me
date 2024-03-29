package ru.yandex.practicum.mainservice.event.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.mainservice.category.dto.CategoryDto;
import ru.yandex.practicum.mainservice.user.dto.UserDto;

import java.time.LocalDateTime;


/**
 * ShortEventDto возвращается в http-ответах, когда требуется краткая информация о событии
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortEventDto {

    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;
    private Long id;
    private UserDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
