package ru.yandex.practicum.mainservice.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.mainservice.category.model.Category;
import ru.yandex.practicum.mainservice.category.repository.CategoryRepository;
import ru.yandex.practicum.mainservice.comment.dto.CommentDto;
import ru.yandex.practicum.mainservice.comment.mapper.CommentMapper;
import ru.yandex.practicum.mainservice.comment.model.Comment;
import ru.yandex.practicum.mainservice.comment.model.QComment;
import ru.yandex.practicum.mainservice.event.dto.CreatedEventDto;
import ru.yandex.practicum.mainservice.event.dto.EventDto;
import ru.yandex.practicum.mainservice.event.dto.ShortEventDto;
import ru.yandex.practicum.mainservice.event.dto.UpdatedEventDto;
import ru.yandex.practicum.mainservice.event.mapper.EventMapper;
import ru.yandex.practicum.mainservice.event.mapper.LocationMapper;
import ru.yandex.practicum.mainservice.event.model.Event;
import ru.yandex.practicum.mainservice.event.model.Location;
import ru.yandex.practicum.mainservice.event.model.QEvent;
import ru.yandex.practicum.mainservice.event.model.State;
import ru.yandex.practicum.mainservice.event.repository.EventRepository;
import ru.yandex.practicum.mainservice.event.repository.LocationRepository;
import ru.yandex.practicum.mainservice.exceptions.DateTimeValidationException;
import ru.yandex.practicum.mainservice.exceptions.InvalidStateException;
import ru.yandex.practicum.mainservice.exceptions.NotFoundException;
import ru.yandex.practicum.mainservice.exceptions.WrongInitiatorException;
import ru.yandex.practicum.mainservice.request.model.RequestStatus;
import ru.yandex.practicum.mainservice.user.model.User;
import ru.yandex.practicum.mainservice.user.repository.UserRepository;
import ru.yandex.practicum.statsclient.StatsClient;
import ru.yandex.practicum.statsdto.hit.CreatedHitDto;
import ru.yandex.practicum.statsdto.hit.StatsDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.yandex.practicum.mainservice.request.model.QRequest.request;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;
    private final CommentMapper commentMapper;

    @PersistenceContext
    private final EntityManager entityManager;


    @Override
    public List<ShortEventDto> getEventListByInitiatorId(Long userId, Long from, Long size) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id: " + userId + " не найден"));

        QEvent qEvent = QEvent.event;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        List<Event> eventList = queryFactory
                .selectFrom(qEvent)
                .where(qEvent.initiator.id.eq(userId))
                .offset(from).limit(size)
                .fetch();

        return eventList.stream()
                .map(event -> {
                    setViewsAndConfirmedRequests(event);
                    return eventMapper.eventToShortEventDto(event);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventDto createEvent(Long userId, CreatedEventDto createdEventDto) {
        User initiator = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id: " + userId + " не найден"));

        long categoryId = createdEventDto.getCategory();
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("Категория с id: " + categoryId + " не найдена"));

        Event event = eventMapper.createdEventDtoToEvent(createdEventDto);
        event.setInitiator(initiator);
        event.setCategory(category);
        Event savedEvent = eventRepository.save(event);

        Location location = locationMapper.locationDtoToLocation(createdEventDto.getLocation());
        location.setId(savedEvent.getId());
        locationRepository.save(location);
        setViewsAndConfirmedRequests(savedEvent);
        savedEvent.setComments(getEventComments(savedEvent.getId()));

        return eventMapper.eventToEventDto(savedEvent);
    }

    @Override
    public EventDto getFullEventInfoByInitiator(Long userId, Long eventId) {
        User initiator = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id: " + userId + " не найден"));

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id: " + eventId + " не найдено"));

        if (!event.getInitiator().getId().equals(initiator.getId())) {
            throw new WrongInitiatorException("Событие с id: " + eventId + " не было создано пользователем с id: "
                    + userId);
        }

        Location location = locationRepository.findById(event.getId()).orElseThrow(() ->
                new NotFoundException("Координаты для события с id: " + eventId + " не найдены"));
        event.setLocation(location);
        setViewsAndConfirmedRequests(event);
        event.setComments(getEventComments(event.getId()));

        return eventMapper.eventToEventDto(event);
    }

    @Override
    @Transactional
    public EventDto updateEventByInitiator(Long userId, Long eventId, UpdatedEventDto updatedEventDto) {
        User initiator = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id: " + userId + " не найден"));

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id: " + eventId + " не найдено"));

        if (!event.getInitiator().getId().equals(initiator.getId())) {
            throw new WrongInitiatorException("Событие с id: " + eventId + " не было создано пользователем с id: "
                    + userId);
        }

        if (event.getState().equals(State.PUBLISHED)) {
            throw new InvalidStateException("Невозможно обновить опубликованные события");
        }

        Event eventToUpdate = eventMapper.updatedEventDtoToEvent(updatedEventDto);
        event.updateWith(eventToUpdate);

        Long categoryId = updatedEventDto.getCategory();
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                    new NotFoundException("Категория с id: " + categoryId + " не найдена"));
            event.setCategory(category);
        }

        if (updatedEventDto.getStateAction() != null) {
            switch (updatedEventDto.getStateAction()) {
                case CANCEL_REVIEW:
                    if (!event.getState().equals(State.PENDING)) {
                        throw new InvalidStateException("Отклонять можно только события в статусе " +
                                "ожидания");
                    }
                    event.setState(State.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    if (!event.getState().equals(State.CANCELED)) {
                        throw new InvalidStateException("Отправлять на ревью можно только отмененные события");
                    }
                    event.setState(State.PENDING);
                    break;
                default:
                    throw new InvalidStateException("Некорректный запрос на изменение статусв: "
                            + updatedEventDto.getStateAction());
            }
        }

        Event updatedEvent = eventRepository.save(event);
        setViewsAndConfirmedRequests(updatedEvent);
        updatedEvent.setComments(getEventComments(updatedEvent.getId()));

        if (updatedEventDto.getLocation() != null) {
            Location updatedLocation = locationMapper.locationDtoToLocation(updatedEventDto.getLocation());
            updatedLocation.setId(event.getId());
            locationRepository.save(updatedLocation);
            updatedEvent.setLocation(updatedLocation);
        } else {
            Location location = locationRepository.findById(event.getId()).orElseThrow(() ->
                    new NotFoundException("Координаты для события с id: " + eventId + " не найдены"));
            updatedEvent.setLocation(location);
        }
        return eventMapper.eventToEventDto(updatedEvent);
    }

    @Override
    @Transactional
    public List<EventDto> getEventListByAdminParameters(Integer[] userIds, String[] states, Integer[] categories,
                                                        String rangeStart, String rangeEnd, Long from, Long size) {

        if (rangeStart != null && rangeEnd != null) {
            checkDates(rangeStart, rangeEnd);
        }

        QEvent qEvent = QEvent.event;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        BooleanExpression condition = qEvent.isNotNull();

        if (userIds != null && userIds.length > 0) {
            condition = condition.and(qEvent.initiator.id.in(userIds));
        }

        if (states != null && states.length > 0) {
            condition = condition.and(qEvent.state.stringValue().in(states));
        }

        if (categories != null && categories.length > 0) {
            condition = condition.and(qEvent.category.id.in(categories));
        }

        if (rangeStart != null) {
            LocalDateTime startDateTime = decodeDateTime(rangeStart);
            condition = condition.and(qEvent.eventDate.after(startDateTime));
        }

        if (rangeEnd != null) {
            LocalDateTime endDateTime = decodeDateTime(rangeEnd);
            condition = condition.and(qEvent.eventDate.before(endDateTime));
        }

        List<Event> eventList = queryFactory
                .selectFrom(qEvent)
                .where(condition)
                .offset(from).limit(size)
                .fetch();

        return eventList.stream()
                .map(event -> {
                    Location location = locationRepository.findById(event.getId()).orElseThrow(() ->
                            new NotFoundException("Координаты для события с id: " + event.getId() + " не найдены"));
                    event.setLocation(location);
                    setViewsAndConfirmedRequests(event);
                    event.setComments(getEventComments(event.getId()));
                    return eventMapper.eventToEventDto(event);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventDto updateEventByAdmin(Long eventId, UpdatedEventDto updatedEventDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id: " + eventId + " не найдено"));

        Event eventToUpdate = eventMapper.updatedEventDtoToEvent(updatedEventDto);
        event.updateWith(eventToUpdate);

        Long categoryId = updatedEventDto.getCategory();
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                    new NotFoundException("Категория с id: " + categoryId + " не найдена"));
            event.setCategory(category);
        }

        if (updatedEventDto.getStateAction() != null) {
            if (!event.getState().equals(State.PENDING)) {
                throw new InvalidStateException("Публиковать или отклонять можно только события в статусе " +
                        "ожидания");
            }
            switch (updatedEventDto.getStateAction()) {
                case PUBLISH_EVENT:
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    event.setState(State.CANCELED);
                    break;
                default:
                    throw new InvalidStateException("Некорректный запрос на изменение статусв: "
                            + updatedEventDto.getStateAction());
            }
        }

        Event updatedEvent = eventRepository.save(event);
        setViewsAndConfirmedRequests(updatedEvent);
        updatedEvent.setComments(getEventComments(updatedEvent.getId()));

        if (updatedEventDto.getLocation() != null) {
            Location updatedLocation = locationMapper.locationDtoToLocation(updatedEventDto.getLocation());
            updatedLocation.setId(event.getId());
            locationRepository.save(updatedLocation);
            updatedEvent.setLocation(updatedLocation);
        } else {
            Location location = locationRepository.findById(event.getId()).orElseThrow(() ->
                    new NotFoundException("Координаты для события с id: " + eventId + " не найдены"));
            updatedEvent.setLocation(location);
        }
        return eventMapper.eventToEventDto(updatedEvent);
    }

    @Override
    public List<ShortEventDto> getEventListByPublicParameters(String text, Integer[] categories, Boolean paid,
                                                              String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                              String sort, Long from, Long size,
                                                              HttpServletRequest request) {

        if (rangeStart != null && rangeEnd != null) {
            checkDates(rangeStart, rangeEnd);
        }

        QEvent qEvent = QEvent.event;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        BooleanExpression condition = qEvent.state.eq(State.PUBLISHED);

        if (text != null && !text.isEmpty()) {
            String searchText = text.toLowerCase();
            condition = condition.and(qEvent.annotation.toLowerCase().like("%" + searchText + "%")
                    .or(qEvent.description.toLowerCase().like("%" + searchText + "%")));
        }

        if (categories != null && categories.length > 0) {
            condition = condition.and(qEvent.category.id.in(categories));
        }

        if (paid != null) {
            condition = condition.and(qEvent.paid.eq(paid));
        }

        if (rangeStart != null) {
            LocalDateTime startDateTime = decodeDateTime(rangeStart);
            condition = condition.and(qEvent.eventDate.after(startDateTime));
        }

        if (rangeEnd != null) {
            LocalDateTime endDateTime = decodeDateTime(rangeEnd);
            condition = condition.and(qEvent.eventDate.before(endDateTime));
        }

        if (rangeStart == null && rangeEnd == null) {
            condition = condition.and(qEvent.eventDate.after(LocalDateTime.now()));
        }

        JPAQuery<Event> query = queryFactory
                .selectFrom(qEvent)
                .where(condition)
                .offset(from).limit(size);

        if ("EVENT_DATE".equals(sort)) {
            query = query.orderBy(qEvent.eventDate.asc());
        }

        List<Event> eventList = query.fetch();

        List<ShortEventDto> eventDtoList = eventList.stream()
                .map(event -> {
                    long confirmedRequests = getConfirmedRequests(event.getId());
                    if (Boolean.TRUE.equals(onlyAvailable) && event.getParticipantLimit() > 0 &&
                            confirmedRequests >= event.getParticipantLimit()) {
                        return null;
                    } else {
                        setViewsAndConfirmedRequests(event);
                        return eventMapper.eventToShortEventDto(event);
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());


        if ("VIEWS".equals(sort)) {
            eventDtoList = eventDtoList.stream()
                    .sorted((d1, d2) -> d2.getViews().compareTo(d1.getViews()))
                    .collect(Collectors.toList());
        }

        saveHit(request);
        return eventDtoList;
    }

    @Override
    public EventDto getPublishedEventInfoById(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id: " + eventId + " не найдено"));

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("Событие с id: " + eventId + " не найдено");
        }

        Location location = locationRepository.findById(event.getId()).orElseThrow(() ->
                new NotFoundException("Координаты для события с id: " + eventId + " не найдены"));
        event.setLocation(location);
        setViewsAndConfirmedRequests(event);
        event.setComments(getEventComments(event.getId()));

        saveHit(request);

        return eventMapper.eventToEventDto(event);
    }

    @Override
    public Event setViewsAndConfirmedRequests(Event event) {
        event.setConfirmedRequests(getConfirmedRequests(event.getId()));
        event.setViews(getViews(event));
        return event;
    }


    private Long getViews(Event event) {
        String start = encodeDateTime(event.getCreatedOn());
        String end = encodeDateTime(LocalDateTime.now());
        String expectedUri = "/events/" + event.getId();
        String[] uriArray = {expectedUri};

        ResponseEntity<List<StatsDto>> response = statsClient.getStats(start, end, uriArray, true);

        if (response.getBody() == null || response.getBody().size() != 1) {
            return 0L;
        }

        StatsDto stats = response.getBody().get(0);
        if (!expectedUri.equals(stats.getUri())) {
            return 0L;
        }

        return stats.getHits();
    }

    private String encodeDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String stringDateTime = dateTime.format(formatter);
        return URLEncoder.encode(stringDateTime, StandardCharsets.UTF_8);
    }

    private LocalDateTime decodeDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String decodedDateTime = URLDecoder.decode(dateTime, StandardCharsets.UTF_8);

        try {
            return LocalDateTime.parse(decodedDateTime, formatter);
        } catch (DateTimeParseException e) {
            throw new DateTimeValidationException("Некорректный формат времени: " + dateTime);
        }
    }

    private void checkDates(String rangeStart, String rangeEnd) {
        LocalDateTime startDateTime = decodeDateTime(rangeStart);
        LocalDateTime endDateTime = decodeDateTime(rangeEnd);

        if (startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("Время начала диапазона поиска " + startDateTime + " не может быть " +
                    "позже времени конца диапазона поиска " + endDateTime);
        }
    }

    private Long getConfirmedRequests(Long eventId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        return queryFactory.select(request.count())
                .from(request)
                .where(request.event.eq(eventId)
                        .and(request.status.eq(RequestStatus.CONFIRMED)))
                .fetchOne();
    }

    private void saveHit(HttpServletRequest request) {
        CreatedHitDto createdHitDto = CreatedHitDto.builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();

        statsClient.createHit(createdHitDto);
    }

    private List<CommentDto> getEventComments(Long eventId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QComment qComment = QComment.comment;

        List<Comment> commentList = queryFactory
                .selectFrom(qComment)
                .where(qComment.event.id.eq(eventId))
                .orderBy(qComment.created.desc())
                .fetch();

        return commentList.stream()
                .map(commentMapper::CommentToCommentDto)
                .collect(Collectors.toList());
    }
}