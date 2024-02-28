package ru.yandex.practicum.mainservice.request.service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.mainservice.event.model.Event;
import ru.yandex.practicum.mainservice.event.model.State;
import ru.yandex.practicum.mainservice.event.repository.EventRepository;
import ru.yandex.practicum.mainservice.exceptions.InvalidStateException;
import ru.yandex.practicum.mainservice.exceptions.NotFoundException;
import ru.yandex.practicum.mainservice.exceptions.WrongInitiatorException;
import ru.yandex.practicum.mainservice.request.dto.RequestDto;
import ru.yandex.practicum.mainservice.request.dto.RequestStatusAggregateDto;
import ru.yandex.practicum.mainservice.request.dto.RequestStatusUpdateDto;
import ru.yandex.practicum.mainservice.request.mapper.RequestMapper;
import ru.yandex.practicum.mainservice.request.model.QRequest;
import ru.yandex.practicum.mainservice.request.model.Request;
import ru.yandex.practicum.mainservice.request.model.RequestStatus;
import ru.yandex.practicum.mainservice.request.repository.RequestRepository;
import ru.yandex.practicum.mainservice.user.model.User;
import ru.yandex.practicum.mainservice.user.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getUserRequestList(Long userId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QRequest qRequest = QRequest.request;

        JPAQuery<RequestDto> query = queryFactory
                .select(Projections.constructor(RequestDto.class,
                        qRequest.created,
                        qRequest.event,
                        qRequest.id,
                        qRequest.requester,
                        qRequest.status))
                .from(qRequest)
                .where(qRequest.requester.eq(userId));
        return query.fetch();
    }

    @Override
    public RequestDto createRequest(Long userId, Long eventId) {
        User requester = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id: " + userId + " не найден"));

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id: " + eventId + " не найдено"));

        if (isRequestIsDublicated(userId, eventId)) {
            throw new InvalidStateException("Запрос на участие в событии с id: " + eventId + " уже добавлен" +
                    " пользователем c id: " + userId + " ранее");
        }

        if (isParticipantLimitIsFull(event)) {
            throw new InvalidStateException("Лимит на количество запросов в событии с id: " + eventId + " уже " +
                    "исчерпан");
        }

        if (event.getInitiator().getId().equals(requester.getId())) {
            throw new InvalidStateException("Инициатор события не может добавить запрос на участие в своём событии");
        }

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new InvalidStateException("Cобытие c id: " + eventId + " еще не опубликовано");
        }

        Request request = Request.builder()
                .requester(requester.getId())
                .event(event.getId())
                .build();

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }

        Request savedRequest = requestRepository.save(request);
        return requestMapper.requestToRequestDto(savedRequest);
    }

    @Override
    public RequestDto cancelRequest(Long userId, Long requestId) {

        Request request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос с id: " + requestId + " не найден"));

        if (!Objects.equals(request.getRequester(), userId)) {
            throw new NotFoundException("Запрос с id: " + userId + " не найден");
        }

        request.setStatus(RequestStatus.CANCELED);
        requestRepository.save(request);

        return requestMapper.requestToRequestDto(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getRequestListForUserEvent(Long userId, Long eventId) {
        User initiator = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id: " + userId + " не найден"));

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id: " + eventId + " не найдено"));

        if (!event.getInitiator().getId().equals(initiator.getId())) {
            throw new WrongInitiatorException("Событие с id: " + eventId + " не было создано пользователем с id: "
                    + userId);
        }

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QRequest qRequest = QRequest.request;

        JPAQuery<RequestDto> query = queryFactory
                .select(Projections.constructor(RequestDto.class,
                        qRequest.created,
                        qRequest.event,
                        qRequest.id,
                        qRequest.requester,
                        qRequest.status))
                .from(qRequest)
                .where(qRequest.event.eq(eventId));

        return query.fetch();
    }

    @Override
    public RequestStatusAggregateDto updateRequestStatusByInitiator(Long userId, Long eventId,
                                                                    RequestStatusUpdateDto requestStatusUpdateDto) {
        User initiator = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id: " + userId + " не найден"));

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id: " + eventId + " не найдено"));

        if (!event.getInitiator().getId().equals(initiator.getId())) {
            throw new WrongInitiatorException("Событие с id: " + eventId + " не было создано пользователем с id: "
                    + userId);
        }

        if (event.getRequestModeration().equals(false) || event.getParticipantLimit() == 0) {
            throw new InvalidStateException("Cобытие c id: " + eventId + " не требует подтверждения заявок");
        }

        List<Integer> requestIds = requestStatusUpdateDto.getRequestIds();
        RequestStatus status = requestStatusUpdateDto.getStatus();

        List<RequestDto> confirmedRequests = new ArrayList<>();
        List<RequestDto> deniedRequests = new ArrayList<>();

        for (Integer requestId : requestIds) {
            Request request = requestRepository.findById(Long.valueOf(requestId)).orElseThrow(() ->
                    new NotFoundException("Запрос с id: " + requestId + " не найден"));

            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                throw new InvalidStateException("Заявка c id: " + requestId + " не находится в статусе PENDING");
            }

            if (isParticipantLimitIsFull(event)) {
                throw new InvalidStateException("Для события c id: " + eventId + " уже достигнут лимит заявок");
            }
            request.setStatus(status);
            requestRepository.save(request);

            if (status.equals(RequestStatus.CONFIRMED)) {
                confirmedRequests.add(requestMapper.requestToRequestDto(request));
            } else {
                deniedRequests.add(requestMapper.requestToRequestDto(request));
            }

            if (isParticipantLimitIsFull(event)) {
                status = RequestStatus.REJECTED;
            }
        }

        return RequestStatusAggregateDto.builder()
                .confirmedRequests(confirmedRequests)
                .deniedRequests(deniedRequests)
                .build();

    }

    private boolean isRequestIsDublicated(Long userId, Long eventId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QRequest qRequest = QRequest.request;

        long count = queryFactory
                .selectFrom(qRequest)
                .where(qRequest.requester.eq(userId)
                        .and(qRequest.event.eq(eventId)))
                .fetch().size();

        return count > 0;
    }

    private boolean isParticipantLimitIsFull(Event event) {
        if (event.getParticipantLimit() == 0) {
            return false;
        }

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QRequest qRequest = QRequest.request;

        long participantCount = queryFactory
                .selectFrom(qRequest)
                .where(qRequest.event.eq(event.getId())
                        .and(qRequest.status.eq(RequestStatus.CONFIRMED)))
                .fetch().size();

        return participantCount >= event.getParticipantLimit();
    }
}
