package ru.yandex.practicum.mainservice.event.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.mainservice.event.validator.FutureWithMinOffset;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedEventDto {


    @NotBlank(message = "Не указана аннотация")
    @Size(min = 20, max = 2000, message = "Размер аннотации должен быть не менее 20 и не более 2000 символов")
    private String annotation;

    @NotNull(message = "Не указан id категории")
    @Positive(message = "id категории должен быть положительным числом")
    private Long category;

    @NotBlank(message = "Не указано описание")
    @Size(min = 20, message = "Размер описания должен быть не менее 20 символов")
    private String description;

    @FutureWithMinOffset
    private LocalDateTime eventDate;

    @NotNull(message = "Не указаны координаты события")
    private LocationDto location;

    @NotNull(message = "Не указано, платное событие или бесплатное")
    private Boolean paid;

    @NotNull(message = "Не указан лимит на количество участников")
    @Positive(message = "id категории должен быть положительным числом")
    private Long participantLimit;

    @NotNull(message = "Не указано, требуется ли модерация запросов на участие")
    private Boolean requestModeration;

    String stateAction;

    @NotBlank(message = "Не указан заголовок")
    private String title;
}
