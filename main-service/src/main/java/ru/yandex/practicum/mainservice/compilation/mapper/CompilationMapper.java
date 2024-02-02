package ru.yandex.practicum.mainservice.compilation.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.mainservice.compilation.dto.CompilationDto;
import ru.yandex.practicum.mainservice.compilation.dto.CreatedCompilationDto;
import ru.yandex.practicum.mainservice.compilation.model.Compilation;
import ru.yandex.practicum.mainservice.event.mapper.EventMapper;

import java.util.stream.Collectors;

/**
 * CompilationMapper дл маппинга CompilationDto
 */
@Component
@RequiredArgsConstructor
public class CompilationMapper {

    private final EventMapper eventMapper;

    public Compilation CompilationDtoToCompilation(CreatedCompilationDto createdCompilationDto) {
        return Compilation.builder()
                .pinned(createdCompilationDto.getPinned())
                .title(createdCompilationDto.getTitle())
                .build();
    }

    public CompilationDto CompilationToCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .events(compilation.getEvents().stream()
                        .map(eventMapper::eventToShortEventDto)
                        .collect(Collectors.toList()))
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }
}
