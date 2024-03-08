package ru.yandex.practicum.mainservice.comment.service;

import ru.yandex.practicum.mainservice.comment.dto.CommentDto;
import ru.yandex.practicum.mainservice.comment.dto.CreatedCommentDto;

public interface CommentService {

    CommentDto createCommentByUser(Long userId, Long eventId, CreatedCommentDto createdCommentDto);

    CommentDto updateCommentByUser(Long userId, Long commentId, CreatedCommentDto createdCommentDto);

    void deleteCommentByUser(Long userId, Long commentId);

    void deleteCommentByAdmin(Long commentId);


}
