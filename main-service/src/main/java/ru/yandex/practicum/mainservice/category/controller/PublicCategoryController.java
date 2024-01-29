package ru.yandex.practicum.mainservice.category.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.mainservice.category.dto.CategoryDto;
import ru.yandex.practicum.mainservice.category.service.CategoryService;
import ru.yandex.practicum.mainservice.exceptions.NotFoundException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class PublicCategoryController {

    private final CategoryService categoryService;


    /**
     * Получение списка категорий по заданным параметрам
     *
     * @param from индекс, с которого будет начинаться список
     * @param size размер списка
     * @return List<CategoryDto>
     */

    @GetMapping
    ResponseEntity<List<CategoryDto>> getCategoryList(@RequestParam(defaultValue = "0") Long from,
                                                      @RequestParam(defaultValue = "10") Long size) {
        if (from < 0) {
            throw new IllegalStateException("Индекс запроса не может быть меньше нуля");
        }
        if (size < 1) {
            throw new IllegalStateException("Размер списка не может быть меньше 1");
        }
        List<CategoryDto> categoryList = categoryService.getCategoryList(from, size);
        log.debug("Получен список всех пользователей с from={}, size={}, список={}", from, size, categoryList);
        return ResponseEntity.ok(categoryList);
    }

    /**
     * Получение информации о категории по ее id
     *
     * @param catId id требуемой категории
     * @return CategoryDto
     */

    @GetMapping(path = "/{catId}")
    ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long catId) {
        if (catId <= 0) {
            throw new NotFoundException("Id пользователя должен быть положительным числом");
        }

        CategoryDto category = categoryService.getCategoryById(catId);
        log.debug("Получена информация о категории {}", category);
        return ResponseEntity.ok(category);
    }
}
