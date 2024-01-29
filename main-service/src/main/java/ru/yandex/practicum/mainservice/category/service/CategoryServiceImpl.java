package ru.yandex.practicum.mainservice.category.service;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.mainservice.category.dto.CategoryDto;
import ru.yandex.practicum.mainservice.category.dto.CreatedCategoryDto;
import ru.yandex.practicum.mainservice.category.mapper.CategoryMapper;
import ru.yandex.practicum.mainservice.category.model.Category;
import ru.yandex.practicum.mainservice.category.model.QCategory;
import ru.yandex.practicum.mainservice.category.repository.CategoryRepository;
import ru.yandex.practicum.mainservice.exceptions.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public CategoryDto createCategory(CreatedCategoryDto createdCategoryDto) {
        Category savedCategory =
                categoryRepository.save(categoryMapper.createdCategoryDtoToCategory(createdCategoryDto));
        return categoryMapper.categoryToCategoryDto(savedCategory);
    }

    @Override
    public void deleteCategory(Long catId) {
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(Long catId, CreatedCategoryDto createdCategoryDto) {
        Category updatedCategory = categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException("Категория с id: " + catId + " не найдена"));
        updatedCategory.setName(createdCategoryDto.getName());
        return categoryMapper.categoryToCategoryDto(categoryRepository.save(updatedCategory));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategoryList(Long from, Long size) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QCategory qCategory = QCategory.category;

        JPAQuery<CategoryDto> query = queryFactory
                .select(Projections.constructor(CategoryDto.class,
                        qCategory.id,
                        qCategory.name))
                .from(qCategory)
                .orderBy(qCategory.id.asc())
                .offset(from).limit(size);

        return query.fetch();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException("Категория с id: " + catId + " не найдена"));
        return categoryMapper.categoryToCategoryDto(category);
    }
}
