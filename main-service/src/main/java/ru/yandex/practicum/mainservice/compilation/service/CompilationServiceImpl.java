package ru.yandex.practicum.mainservice.compilation.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.mainservice.compilation.dto.CompilationDto;
import ru.yandex.practicum.mainservice.compilation.dto.CreatedCompilationDto;
import ru.yandex.practicum.mainservice.compilation.mapper.CompilationMapper;
import ru.yandex.practicum.mainservice.compilation.model.Compilation;
import ru.yandex.practicum.mainservice.compilation.model.QCompilation;
import ru.yandex.practicum.mainservice.compilation.repository.CompilationRepository;
import ru.yandex.practicum.mainservice.event.model.Event;
import ru.yandex.practicum.mainservice.event.repository.EventRepository;
import ru.yandex.practicum.mainservice.event.service.EventService;
import ru.yandex.practicum.mainservice.exceptions.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Transactional
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;
    private final EventService eventService;

    @PersistenceContext
    private final EntityManager entityManager;


    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilationList(Boolean pinned, Long from, Long size) {
        QCompilation qCompilation = QCompilation.compilation;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        BooleanExpression condition = qCompilation.isNotNull();

        if (pinned != null) {
            condition = condition.and(qCompilation.pinned.eq(pinned));
        }

        List<Compilation> compilationList = queryFactory
                .selectFrom(qCompilation)
                .where(condition)
                .offset(from).limit(size)
                .fetch();

        for (Compilation compilation : compilationList) {
            if (compilation.getEvents() != null && !compilation.getEvents().isEmpty()) {
                List<Event> eventList = compilation.getEvents().stream()
                        .map(eventService::setViewsAndConfirmedRequests)
                        .collect(Collectors.toList());
                compilation.setEvents(eventList);
            }
        }

        return compilationList.stream()
                .map(compilationMapper::CompilationToCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Подборка с id: " + compId + " не найдена"));
        List<Event> events = compilation.getEvents().stream()
                .map(eventService::setViewsAndConfirmedRequests)
                .collect(Collectors.toList());
        compilation.setEvents(events);

        return compilationMapper.CompilationToCompilationDto(compilation);
    }

    @Override
    public CompilationDto createCompilation(CreatedCompilationDto createdCompilationDto) {

        Compilation compilationToSave = compilationMapper.CompilationDtoToCompilation(createdCompilationDto);
        List<Long> eventIds = createdCompilationDto.getEvents();
        if (eventIds != null && !eventIds.isEmpty()) {
            List<Event> eventList = eventRepository.findAllById(eventIds);
            compilationToSave.setEvents(eventList);
        }

        Compilation savedCompilation = compilationRepository.save(compilationToSave);
        if (savedCompilation.getEvents() != null && !savedCompilation.getEvents().isEmpty()) {
            List<Event> eventList = savedCompilation.getEvents().stream()
                    .map(eventService::setViewsAndConfirmedRequests)
                    .collect(Collectors.toList());
            savedCompilation.setEvents(eventList);
        }
        return compilationMapper.CompilationToCompilationDto(savedCompilation);
    }

    @Override
    public void deleteCompilationById(Long compId) {
        compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Подборка с id: " + compId + " не найдена"));
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilationById(Long compId, CreatedCompilationDto createdCompilationDto) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Подборка с id: " + compId + " не найдена"));

        Compilation compilationToUpdate = compilationMapper.CompilationDtoToCompilation(createdCompilationDto);
        compilation.updateWith(compilationToUpdate);

        List<Long> eventIds = createdCompilationDto.getEvents();
        if (eventIds != null && !eventIds.isEmpty()) {
            List<Event> eventList = eventRepository.findAllById(eventIds);
            compilation.setEvents(eventList);
        }

        Compilation updatedCompilation = compilationRepository.save(compilation);
        List<Event> events = updatedCompilation.getEvents().stream()
                .map(eventService::setViewsAndConfirmedRequests)
                .collect(Collectors.toList());
        updatedCompilation.setEvents(events);
        return compilationMapper.CompilationToCompilationDto(updatedCompilation);
    }
}
