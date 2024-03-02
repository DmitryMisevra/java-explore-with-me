package ru.yandex.practicum.mainservice.compilation.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.mainservice.compilation.dto.CompilationDto;
import ru.yandex.practicum.mainservice.compilation.dto.CreatedCompilationDto;
import ru.yandex.practicum.mainservice.compilation.dto.UpdatedCompilationDto;
import ru.yandex.practicum.mainservice.compilation.model.Compilation;
import ru.yandex.practicum.mainservice.event.mapper.EventMapper;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CompilationMapper дл маппинга CompilationDto
 */
@Component
@RequiredArgsConstructor
public class CompilationMapper {

    private final EventMapper eventMapper;

    public Compilation createdCompilationDtoToCompilation(CreatedCompilationDto createdCompilationDto) {
        return Compilation.builder()
                .pinned(createdCompilationDto.getPinned())
                .title(createdCompilationDto.getTitle())
                .build();
    }

    public Compilation updatedCompilationDtoToCompilation(UpdatedCompilationDto updatedCompilationDto) {
        return Compilation.builder()
                .pinned(updatedCompilationDto.getPinned())
                .title(updatedCompilationDto.getTitle())
                .build();
    }

    public CompilationDto CompilationToCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(Optional.ofNullable(compilation.getEvents())
                        .orElseGet(Collections::emptyList) // Возвращает пустой список, если getEvents() == null
                        .stream()
                        .map(eventMapper::eventToShortEventDto)
                        .collect(Collectors.toList()))
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }
}
