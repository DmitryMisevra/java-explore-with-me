package ru.yandex.practicum.mainservice.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;


/**
 * UpdatedCompilationDto передается при обновлении подборки
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedCompilationDto {

    private List<Long> events;

    @Builder.Default
    private Boolean pinned = false;

    @Size(max = 50, message = "Размер заголовка подборки должен быть не более 50 символов")
    private String title;
}
