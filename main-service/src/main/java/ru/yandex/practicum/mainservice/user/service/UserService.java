package ru.yandex.practicum.mainservice.user.service;

import ru.yandex.practicum.mainservice.user.dto.CreatedUserDto;
import ru.yandex.practicum.mainservice.user.dto.UserDto;

import java.util.List;

public interface UserService {


    UserDto createUser(CreatedUserDto createdUserDto);

    List<UserDto> getUserList(Integer[] uris, Long from, Long size);

    void deleteUserById(long id);


}
