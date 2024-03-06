package ru.yandex.practicum.mainservice.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.mainservice.comment.dto.CommentDto;
import ru.yandex.practicum.mainservice.comment.service.CommentService;

import javax.validation.constraints.Min;

@Slf4j
@RestController
@RequestMapping(path = "admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    private final CommentService commentService;

    /**
     * Удалить комментарий администратором
     *
     * @param commentId id комментария
     * @return HttpStatus.NO_CONTENT
     */
    @DeleteMapping(path = "/{commentId}")
    ResponseEntity<CommentDto> deleteCommentByAdmin(@PathVariable @Min(1) Long commentId) {
        commentService.deleteCommentByAdmin(commentId);
        log.debug("Админом удален комментарий с id {}", commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
