package ru.yandex.practicum.mainservice.compilation.controller;


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
import ru.yandex.practicum.mainservice.compilation.dto.CompilationDto;
import ru.yandex.practicum.mainservice.compilation.dto.CreatedCompilationDto;
import ru.yandex.practicum.mainservice.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {

    private final CompilationService compilationService;

    /**
     * Создать новую подборку
     *
     * @param createdCompilationDto createdCompilationDto
     * @return CompilationDto
     */
    @PostMapping
    ResponseEntity<CompilationDto> createCompilation(@Valid @RequestBody CreatedCompilationDto createdCompilationDto) {
        CompilationDto compilationDto = compilationService.createCompilation(createdCompilationDto);
        log.debug("Создана новая подборка: {}", compilationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(compilationDto);
    }

    /**
     * Удалить подборку по ее id
     *
     * @param compId id подборки
     * @return сообщение об успешном удалении
     */
    @DeleteMapping(path = "{compId}")
    ResponseEntity<String> deleteCompilationById(@PathVariable @Min(1) Long compId) {
        compilationService.deleteCompilationById(compId);
        log.debug("Подборка c id {} удалена", compId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Подборка с id: " + compId + " успешно удалена");
    }

    /**
     * Обновить подборку по ее id
     *
     * @param compId id подборки
     * @return CompilationDto
     */
    @PatchMapping(path = "{compId}")
    ResponseEntity<CompilationDto> updateCompilationById(@PathVariable @Min(1) Long compId,
                                                         @Valid @RequestBody CreatedCompilationDto createdCompilationDto
    ) {
        CompilationDto compilationDto = compilationService.updateCompilationById(compId, createdCompilationDto);
        log.debug("Подборка c id {} обновлена: {}", compId, compilationDto);
        return ResponseEntity.ok(compilationDto);
    }
}
