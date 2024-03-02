package ru.yandex.practicum.mainservice.user.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.mainservice.user.dto.CreatedUserDto;
import ru.yandex.practicum.mainservice.user.dto.UserDto;
import ru.yandex.practicum.mainservice.user.model.User;

/**
 * Mapper для маппинга dto
 */

@Component
public class UserMapper {

    public User createdUserDtoToUser(CreatedUserDto createdUserDto) {
        return User.builder()
                .name(createdUserDto.getName())
                .email(createdUserDto.getEmail())
                .build();
    }

    public UserDto userToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
