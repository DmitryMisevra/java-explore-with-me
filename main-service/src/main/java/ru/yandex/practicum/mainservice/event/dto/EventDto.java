package ru.yandex.practicum.mainservice.event.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.mainservice.category.dto.CategoryDto;
import ru.yandex.practicum.mainservice.event.model.State;
import ru.yandex.practicum.mainservice.user.dto.UserDto;

import java.time.LocalDateTime;

/**
 * EventDto возвращается в http-ответах, когда требуется полная информация о событии
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {

    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    private Long id;
    private UserDto initiator;
    private LocationDto location;
    private Boolean paid;
    private Long participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private String title;
    private State state;
    private Long views;
}
