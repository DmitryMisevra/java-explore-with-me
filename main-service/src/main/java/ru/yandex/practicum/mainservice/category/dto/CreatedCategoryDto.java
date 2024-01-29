package ru.yandex.practicum.mainservice.category.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * CreatedCategoryDto передается gри создании Category
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatedCategoryDto {

    @NotBlank(message = "Не указано имя категории")
    @Size(max = 50, message = "Имя категории не должно превышать 50 символов")
    private String name;
}
