package ru.yandex.practicum.mainservice.comment.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.mainservice.comment.dto.CommentDto;
import ru.yandex.practicum.mainservice.comment.dto.CreatedCommentDto;
import ru.yandex.practicum.mainservice.comment.mapper.CommentMapper;
import ru.yandex.practicum.mainservice.comment.model.Comment;
import ru.yandex.practicum.mainservice.comment.repository.CommentRepository;
import ru.yandex.practicum.mainservice.event.model.Event;
import ru.yandex.practicum.mainservice.event.model.State;
import ru.yandex.practicum.mainservice.event.repository.EventRepository;
import ru.yandex.practicum.mainservice.exceptions.NotFoundException;
import ru.yandex.practicum.mainservice.exceptions.WrongInitiatorException;
import ru.yandex.practicum.mainservice.user.model.User;
import ru.yandex.practicum.mainservice.user.repository.UserRepository;

@Service
@Transactional
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {


    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private EventRepository eventRepository;
    private CommentMapper commentMapper;

    @Override
    public CommentDto createCommentByUser(Long userId, Long eventId, CreatedCommentDto createdCommentDto) {

        User creator = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id: " + userId + " не найден"));

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id: " + eventId + " не найдено"));

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("Событие с id: " + eventId + " не найдено");
        }

        Comment comment = commentMapper.createdCommentDtoToComment(createdCommentDto);
        comment.setCreator(creator);
        comment.setEvent(event);

        Comment savedComment = commentRepository.save(comment);

        return commentMapper.CommentToCommentDto(savedComment);
    }

    @Override
    public CommentDto updateCommentByUser(Long userId, Long commentId, CreatedCommentDto createdCommentDto) {
        Comment commentToUpdate = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Комментарий с id: " + commentId + " не найден"));

        if (!commentToUpdate.getCreator().getId().equals(userId)) {
            throw new WrongInitiatorException("Комментарий с id: " + commentId + " не был создано пользователем" +
                    " с id: " + userId);
        }

        commentToUpdate.setText(createdCommentDto.getText());
        Comment updatedComment = commentRepository.save(commentToUpdate);

        return commentMapper.CommentToCommentDto(updatedComment);
    }

    @Override
    public void deleteCommentByUser(Long userId, Long commentId) {
        Comment commentToDelete = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Комментарий с id: " + commentId + " не найден"));

        if (!commentToDelete.getCreator().getId().equals(userId)) {
            throw new WrongInitiatorException("Комментарий с id: " + commentId + " не был создано пользователем" +
                    " с id: " + userId);
        }

        commentRepository.deleteById(commentToDelete.getId());
    }

    @Override
    public void deleteCommentByAdmin(Long commentId) {
        Comment commentToDelete = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Комментарий с id: " + commentId + " не найден"));
        commentRepository.deleteById(commentToDelete.getId());
    }
}
