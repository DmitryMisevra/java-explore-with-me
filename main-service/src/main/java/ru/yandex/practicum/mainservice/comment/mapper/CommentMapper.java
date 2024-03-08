package ru.yandex.practicum.mainservice.comment.mapper;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.mainservice.comment.dto.CommentDto;
import ru.yandex.practicum.mainservice.comment.dto.CreatedCommentDto;
import ru.yandex.practicum.mainservice.comment.model.Comment;
import ru.yandex.practicum.mainservice.user.mapper.UserMapper;

/**
 * CommentMapper для маппинга commentDto
 */

@Component
@AllArgsConstructor
public class CommentMapper {


    private final UserMapper userMapper;

    public Comment createdCommentDtoToComment(CreatedCommentDto createdCommentDto) {
        return Comment.builder()
                .text(createdCommentDto.getText())
                .build();
    }

    public CommentDto CommentToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .creator(userMapper.userToUserDto(comment.getCreator()))
                .eventId(comment.getEvent().getId())
                .created(comment.getCreated())
                .build();
    }
}
