package ru.yandex.practicum.mainservice.category.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.mainservice.category.dto.CategoryDto;
import ru.yandex.practicum.mainservice.category.dto.CreatedCategoryDto;
import ru.yandex.practicum.mainservice.category.model.Category;

/**
 * Mapper для маппинга CategoryDto
 */

@Component
public class CategoryMapper {

    public Category createdCategoryDtoToCategory(CreatedCategoryDto createdCategoryDto) {
        return Category.builder()
                .name(createdCategoryDto.getName())
                .build();
    }

    public CategoryDto categoryToCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
