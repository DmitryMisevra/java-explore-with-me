package ru.yandex.practicum.mainservice.compilation.service;

import ru.yandex.practicum.mainservice.compilation.dto.CompilationDto;
import ru.yandex.practicum.mainservice.compilation.dto.CreatedCompilationDto;
import ru.yandex.practicum.mainservice.compilation.dto.UpdatedCompilationDto;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getCompilationList(Boolean pinned, Long from, Long size);

    CompilationDto getCompilationById(Long compId);

    CompilationDto createCompilation(CreatedCompilationDto createdCompilationDto);

    void deleteCompilationById(Long compId);

    CompilationDto updateCompilationById(Long compId, UpdatedCompilationDto updatedCompilationDto);
}
