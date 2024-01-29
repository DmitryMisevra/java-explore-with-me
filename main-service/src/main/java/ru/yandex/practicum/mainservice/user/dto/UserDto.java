package ru.yandex.practicum.mainservice.user.dto;

/**
 * UserDto передается в http-ответе для всех методов UserController
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private String email;
}
