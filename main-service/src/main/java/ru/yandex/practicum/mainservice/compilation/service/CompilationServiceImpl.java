package ru.yandex.practicum.mainservice.compilation.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.mainservice.compilation.dto.CompilationDto;
import ru.yandex.practicum.mainservice.compilation.dto.CreatedCompilationDto;
import ru.yandex.practicum.mainservice.compilation.repository.CompilationRepository;

import java.util.List;


@Service
@AllArgsConstructor
@Transactional
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilationList(Boolean pinned, Long from, Long size) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilationById(Long compId) {
        return null;
    }

    @Override
    public CompilationDto createCompilation(CreatedCompilationDto createdCompilationDto) {
        return null;
    }

    @Override
    public void deleteCompilationById(Long compId) {

    }

    @Override
    public CompilationDto updateCompilationById(Long compId) {
        return null;
    }
}
