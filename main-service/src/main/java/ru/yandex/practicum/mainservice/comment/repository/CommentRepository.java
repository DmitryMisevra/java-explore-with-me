package ru.yandex.practicum.mainservice.comment.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.yandex.practicum.mainservice.comment.model.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, QuerydslPredicateExecutor<Comment> {

    @EntityGraph(value = "comment-with-event-and-creator", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Comment> findById(Long id);
}
