package ru.yandex.practicum.mainservice.event.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.mainservice.event.validator.FutureWithMinOffset;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * UpdatedEventDto передается при обновлении Event
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedEventDto {

    @Size(min = 20, max = 2000, message = "Размер аннотации должен быть не менее 20 и не более 2000 символов")
    private String annotation;

    @Positive(message = "id категории должен быть положительным числом")
    private Long category;

    @Size(min = 20, max = 7000, message = "Размер описания должен быть не менее 20 символов и не более 7000 символов")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureWithMinOffset
    private LocalDateTime eventDate;

    private LocationDto location;
    private Boolean paid;

    @Positive(message = "Лимит участников должен быть положительным числом")
    private Long participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;

    @Size(min = 3, max = 120, message = "Размер заголовка должен быть не менее 3 символов и не более 120 символов")
    private String title;
}
