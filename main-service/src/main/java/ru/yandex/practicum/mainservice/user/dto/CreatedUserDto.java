package ru.yandex.practicum.mainservice.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * CreatedUserDto передается gри создании User
 */

@Data
@Builder
public class CreatedUserDto {

    @NotBlank(message = "не указано имя")
    @Size(min = 2, max = 250, message = "имя должно быть от 2 до 250 символов")
    private String name;
    @NotBlank(message = "не указан Email")
    @Email(message = "неправильный формат Email")
    @Size(min = 6, max = 254, message = "Почта должна быть длиной от 6 до 254 символов")
    private String email;
}
