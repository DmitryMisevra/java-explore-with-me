package ru.yandex.practicum.mainservice.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.mainservice.request.dto.RequestDto;
import ru.yandex.practicum.mainservice.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class PrivateRequestController {

    private final RequestService requestService;

    /**
     * Получить список заявок пользователя
     *
     * @param userId id пользователя
     * @return List<RequestDto>
     */
    @GetMapping
    ResponseEntity<List<RequestDto>> getUserRequestList(@PathVariable @Min(1) Long userId) {
        List<RequestDto> requestList = requestService.getUserRequestList(userId);
        log.debug("Получен список запроса пользователя {}", requestList);
        return ResponseEntity.ok(requestList);
    }

    /**
     * Создать запрос на участие
     *
     * @param userId     id пользователя
     * @param eventId    id события
     * @param requestDto заявка
     * @return RequestDto
     */
    @PostMapping
    ResponseEntity<RequestDto> createRequest(@PathVariable @Min(1) Long userId,
                                             @RequestParam @Min(1) Long eventId,
                                             @Valid @RequestBody RequestDto requestDto) {
        RequestDto createdRequestDto = requestService.createRequest(userId, eventId, requestDto);
        log.debug("Создан запрос на участие: {}", createdRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequestDto);
    }

    /**
     * Отменить запрос на участие
     *
     * @param userId    id пользователя
     * @param requestId id события
     * @return RequestDto
     */
    @PatchMapping(path = "/{requestId}")
    ResponseEntity<RequestDto> cancelRequest(@PathVariable @Min(1) Long userId,
                                             @PathVariable @Min(1) Long requestId) {
        RequestDto canceledRequestDto = requestService.cancelRequest(userId, requestId);
        log.debug("Отменен запрос на участие: {}", canceledRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(canceledRequestDto);
    }
}
