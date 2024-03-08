package ru.yandex.practicum.mainservice.comment.controller;


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
import ru.yandex.practicum.mainservice.comment.dto.CommentDto;
import ru.yandex.practicum.mainservice.comment.dto.CreatedCommentDto;
import ru.yandex.practicum.mainservice.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/comments")
@RequiredArgsConstructor
public class PrivateCommentController {

    private final CommentService commentService;


    /**
     * Добавить комментарий
     *
     * @param userId            id пользователя
     * @param eventId           id комментария
     * @param createdCommentDto dto для создания комментария
     * @return commentDto
     */
    @PostMapping(path = "/{eventId}")
    ResponseEntity<CommentDto> createCommentByUser(@PathVariable @Min(1) Long userId,
                                                   @PathVariable @Min(1) Long eventId,
                                                   @Valid @RequestBody CreatedCommentDto createdCommentDto) {

        CommentDto commentDto = commentService.createCommentByUser(userId, eventId, createdCommentDto);
        log.debug("Добавлен комментарий {}", commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentDto);
    }

    /**
     * Обновить комментарий, оставленный пользователем
     *
     * @param userId            id пользователя
     * @param commentId         id комментария
     * @param createdCommentDto обновленный dto комментария
     * @return commentDto
     */
    @PatchMapping(path = "/{commentId}")
    ResponseEntity<CommentDto> updateCommentByUser(@PathVariable @Min(1) Long userId,
                                                   @PathVariable @Min(1) Long commentId,
                                                   @Valid @RequestBody CreatedCommentDto createdCommentDto) {
        CommentDto commentDto = commentService.updateCommentByUser(userId, commentId, createdCommentDto);
        log.debug("Обновлен комментарий {}", commentDto);
        return ResponseEntity.ok(commentDto);
    }


    /**
     * Удалить комментарий, оставленный пользователем
     *
     * @param userId    id пользователя
     * @param commentId id комментария
     * @return HttpStatus.NO_CONTENT
     */
    @DeleteMapping(path = "/{commentId}")
    ResponseEntity<CommentDto> deleteCommentByUser(@PathVariable @Min(1) Long userId,
                                                   @PathVariable @Min(1) Long commentId) {
        commentService.deleteCommentByUser(userId, commentId);
        log.debug("Пользователем удален комментарий с id {}", commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
