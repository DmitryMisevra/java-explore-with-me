package ru.yandex.practicum.mainservice.category.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.mainservice.category.dto.CategoryDto;
import ru.yandex.practicum.mainservice.category.dto.CreatedCategoryDto;
import ru.yandex.practicum.mainservice.category.service.CategoryService;
import ru.yandex.practicum.mainservice.exceptions.NotFoundException;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    /**
     * Создать новую категорию
     *
     * @param createdCategoryDto createdCategoryDto
     * @return CategoryDto
     */

    @PostMapping
    ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreatedCategoryDto createdCategoryDto) {
        CategoryDto categoryDto = categoryService.createCategory(createdCategoryDto);
        log.debug("добавлена категория {}", categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDto);
    }

    /**
     * Удаление категории по ее id
     *
     * @param catId id удаляемой категории
     * @return Строковое сообщение об успешном удаление
     */

    @DeleteMapping(path = "/{catId}")
    ResponseEntity<String> deleteCategory(@PathVariable Long catId) {
        if (catId <= 0) {
            throw new NotFoundException("Id пользователя должен быть положительным числом");
        }
        categoryService.deleteCategory(catId);
        log.debug("Категория с id={} удалена", catId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Обновить данные о категории по ее id
     *
     * @param catId              id обновляемой категории
     * @param createdCategoryDto createdCategoryDto
     * @return СategoryDto
     */
    @PatchMapping(path = "/{catId}")
    ResponseEntity<CategoryDto> updateCategory(@PathVariable Long catId,
                                               @Valid @RequestBody CreatedCategoryDto createdCategoryDto) {
        CategoryDto categoryDto = categoryService.updateCategory(catId, createdCategoryDto);
        log.debug("Обновлена категория {}", categoryDto);
        return ResponseEntity.ok(categoryDto);
    }
}
