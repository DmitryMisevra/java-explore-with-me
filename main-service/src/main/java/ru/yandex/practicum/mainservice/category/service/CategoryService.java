package ru.yandex.practicum.mainservice.category.service;

import ru.yandex.practicum.mainservice.category.dto.CategoryDto;
import ru.yandex.practicum.mainservice.category.dto.CreatedCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CreatedCategoryDto createdCategoryDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(Long id, CreatedCategoryDto createdCategoryDto);

    List<CategoryDto> getCategoryList(Long from, Long size);

    CategoryDto getCategoryById(Long id);
}
