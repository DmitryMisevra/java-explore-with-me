package ru.yandex.practicum.mainservice.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


/**
 * CreatedCompilationDto передается при создании подборки
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatedCompilationDto {

    @NotNull(message = "не передан список событий для подборки")
    private List<Long> events;

    @Builder.Default
    private Boolean pinned = false;

    @NotBlank(message = "Не указан заголовок подборки")
    @Size(max = 50, message = "Размер заголовка подборки должен быть не более 50 символов")
    private String title;
}
