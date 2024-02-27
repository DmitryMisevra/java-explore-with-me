package ru.yandex.practicum.mainservice.user.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.mainservice.exceptions.NotFoundException;
import ru.yandex.practicum.mainservice.user.dto.CreatedUserDto;
import ru.yandex.practicum.mainservice.user.dto.UserDto;
import ru.yandex.practicum.mainservice.user.service.UserService;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Добавление пользователя
     *
     * @param createdUserDto createdUserDto
     * @return UserDto
     */

    @PostMapping
    ResponseEntity<UserDto> createUser(@Valid @RequestBody CreatedUserDto createdUserDto) {
        UserDto createdUser = userService.createUser(createdUserDto);
        log.debug("Добавлен новый пользователь с id={}", createdUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Получение списка пользователей по параметрам
     *
     * @param ids  cписок id пользователей для выгрузки
     * @param from индекс, с которого выгружается список
     * @param size размер списка
     * @return List<UserDto>
     */

    @GetMapping
    ResponseEntity<List<UserDto>> getUserList(@RequestParam(required = false) Integer[] ids,
                                              @RequestParam(defaultValue = "0") Long from,
                                              @RequestParam(defaultValue = "10") Long size) {
        if (from != null && from < 0) {
            throw new IllegalStateException("Индекс запроса не может быть меньше нуля");
        }
        if (size != null && size < 1) {
            throw new IllegalStateException("Размер списка не может быть меньше 1");
        }
        List<UserDto> userList = userService.getUserList(ids, from, size);
        log.debug("Получен список всех пользователей с from={}, size={}, список={}", from, size, userList);

        return ResponseEntity.ok(userList);
    }

    /**
     * Удаление пользователя по id
     *
     * @param id id пользователя
     * @return ResponseEntity со строковым сообщением
     */

    @DeleteMapping(path = "/{id}")
    ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        if (id <= 0) {
            throw new NotFoundException("Id пользователя должен быть положительным числом");
        }
        userService.deleteUserById(id);
        log.debug("Пользователь с id={} удален", id);
        return ResponseEntity.ok("Пользователь с id: " + id + " успешно удален");
    }
}
