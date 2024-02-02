package ru.yandex.practicum.mainservice.compilation.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.mainservice.compilation.dto.CompilationDto;
import ru.yandex.practicum.mainservice.compilation.service.CompilationService;

import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
public class PublicCompilationController {

    private final CompilationService compilationService;

    /**
     * Получить список подборок
     *
     * @param pinned должна ли подборка быть на главной странице
     * @param from   индекс, с которого должен начинаться поиск
     * @param size   размер списка
     * @return List<CompilationDto>
     */
    @GetMapping
    ResponseEntity<List<CompilationDto>> getCompilationList(@RequestParam(required = false) Boolean pinned,
                                                            @RequestParam(defaultValue = "0") @Min(0) Long from,
                                                            @RequestParam(defaultValue = "10") @Min(1) Long size) {
        List<CompilationDto> compilationDtoList = compilationService.getCompilationList(pinned, from, size);
        log.debug("Получен список подборок: {}", compilationDtoList);
        return ResponseEntity.ok(compilationDtoList);
    }

    /**
     * Получить информацию по ее id
     *
     * @param compId id подборки
     * @return CompilationDto
     */
    @GetMapping(path = "{compId}")
    ResponseEntity<CompilationDto> getCompilationById(@PathVariable @Min(1) Long compId) {
        CompilationDto compilationDto = compilationService.getCompilationById(compId);
        log.debug("Получена информация о подборке: {}", compilationDto);
        return ResponseEntity.ok(compilationDto);
    }
}
