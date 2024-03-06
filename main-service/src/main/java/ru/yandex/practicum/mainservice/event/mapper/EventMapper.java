package ru.yandex.practicum.mainservice.event.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.mainservice.category.mapper.CategoryMapper;
import ru.yandex.practicum.mainservice.event.dto.CreatedEventDto;
import ru.yandex.practicum.mainservice.event.dto.EventDto;
import ru.yandex.practicum.mainservice.event.dto.ShortEventDto;
import ru.yandex.practicum.mainservice.event.dto.UpdatedEventDto;
import ru.yandex.practicum.mainservice.event.model.Event;
import ru.yandex.practicum.mainservice.user.mapper.UserMapper;

/**
 * EventMapper для маппинга eventDto
 */
@Component
@AllArgsConstructor
public class EventMapper {

    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final LocationMapper locationMapper;

    public Event createdEventDtoToEvent(CreatedEventDto createdEventDto) {
        return Event.builder()
                .annotation(createdEventDto.getAnnotation())
                .description(createdEventDto.getDescription())
                .eventDate(createdEventDto.getEventDate())
                .location(locationMapper.locationDtoToLocation(createdEventDto.getLocation()))
                .paid(createdEventDto.getPaid())
                .participantLimit(createdEventDto.getParticipantLimit())
                .requestModeration(createdEventDto.getRequestModeration())
                .title(createdEventDto.getTitle())
                .build();
    }

    public Event updatedEventDtoToEvent(UpdatedEventDto updatedEventDto) {
        return Event.builder()
                .annotation(updatedEventDto.getAnnotation())
                .description(updatedEventDto.getDescription())
                .eventDate(updatedEventDto.getEventDate())
                .paid(updatedEventDto.getPaid())
                .participantLimit(updatedEventDto.getParticipantLimit())
                .requestModeration(updatedEventDto.getRequestModeration())
                .title(updatedEventDto.getTitle())
                .build();
    }

    public EventDto eventToEventDto(Event event) {
        return EventDto.builder()
                .annotation(event.getAnnotation())
                .category(categoryMapper.categoryToCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(userMapper.userToUserDto(event.getInitiator()))
                .location(locationMapper.locationToLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .title(event.getTitle())
                .state(event.getState())
                .views(event.getViews())
                .comments(event.getComments())
                .build();
    }

    public ShortEventDto eventToShortEventDto(Event event) {
        return ShortEventDto.builder()
                .annotation(event.getAnnotation())
                .category(categoryMapper.categoryToCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(userMapper.userToUserDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }
}
