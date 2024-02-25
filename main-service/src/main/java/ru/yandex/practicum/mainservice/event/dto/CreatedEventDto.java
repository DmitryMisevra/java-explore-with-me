package ru.yandex.practicum.mainservice.event.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
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

/**
 * CreatedEventDto передается при создании Event
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatedEventDto {


    @NotBlank(message = "Не указана аннотация")
    @Size(min = 20, max = 2000, message = "Размер аннотации должен быть не менее 20 и не более 2000 символов")
    private String annotation;

    @NotNull(message = "Не указан id категории")
    @Positive(message = "id категории должен быть положительным числом")
    private Long category;

    @NotBlank(message = "Не указано описание")
    @Size(min = 20, max = 7000, message = "Размер описания должен быть не менее 20 символов и не более 7000 символов")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureWithMinOffset
    private LocalDateTime eventDate;

    @NotNull(message = "Не указаны координаты события")
    private LocationDto location;

    @NotNull(message = "Не указано, платное событие или бесплатное")
    private Boolean paid;

    @Positive(message = "Лимит участников должен быть положительным числом")
    @Builder.Default
    private Long participantLimit = 0L;

    @NotNull(message = "Не указано, требуется ли модерация запросов на участие")
    private Boolean requestModeration;

    @NotBlank(message = "Не указан заголовок")
    @Size(min = 3, max = 120, message = "Размер заголовка должен быть не менее 3 символов и не более 120 символов")
    private String title;
}
