package ru.yandex.practicum.statsserver.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.statsdto.hit.CreatedHitDto;
import ru.yandex.practicum.statsdto.hit.HitDto;
import ru.yandex.practicum.statsdto.hit.StatsDto;
import ru.yandex.practicum.statsserver.exceptions.DateTimeValidationException;
import ru.yandex.practicum.statsserver.mapper.HitMapper;
import ru.yandex.practicum.statsserver.model.Hit;
import ru.yandex.practicum.statsserver.model.QHit;
import ru.yandex.practicum.statsserver.repository.StatsRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final HitMapper hitMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public HitDto createHit(CreatedHitDto createdHitDto) {
        Hit savedHit = statsRepository.save(hitMapper.createdHitDtoToHit(createdHitDto));
        return hitMapper.hitToHitDto(savedHit);
    }

    @Override
    public List<StatsDto> getStats(String start, String end, String[] uris, Boolean unique) {

        LocalDateTime startTime = convertStringToLocalDateTime(start);
        LocalDateTime endTime = convertStringToLocalDateTime(end);

        if (startTime.isAfter(endTime)) {
            throw new DateTimeValidationException("Время начала события не может быть позже времени его окончания");
        }

        if (startTime.isEqual(endTime)) {
            throw new DateTimeValidationException("Время начала и окончания события не могут быть одинаковыми");
        }

        BooleanBuilder predicate = new BooleanBuilder(QHit.hit.timestamp.between(startTime, endTime));

        if (uris != null && uris.length > 0) {
            BooleanBuilder urisPredicate = new BooleanBuilder();
            for (String uri : uris) {
                urisPredicate.or(QHit.hit.uri.eq(uri));
            }
            predicate.and(urisPredicate);
        }

        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        QHit qHit = QHit.hit;

        NumberExpression<Long> hitsExpression = unique ? qHit.ip.countDistinct() : qHit.ip.count();


        JPAQuery<StatsDto> jpqlQuery = query
                .select(Projections.constructor(StatsDto.class,
                        qHit.app,
                        qHit.uri,
                        hitsExpression))
                .from(qHit)
                .where(predicate)
                .groupBy(qHit.app, qHit.uri)
                .orderBy(hitsExpression.desc());

        return jpqlQuery.fetch();
    }

    private LocalDateTime convertStringToLocalDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String decodedDateTime = URLDecoder.decode(dateTime, StandardCharsets.UTF_8);

        try {
            return LocalDateTime.parse(decodedDateTime, formatter);
        } catch (DateTimeParseException e) {
            throw new DateTimeValidationException("Некорректный формат времени: " + dateTime);
        }
    }
}
