package ru.yandex.practicum.statsclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.yandex.practicum.statsdto.hit.CreatedHitDto;
import ru.yandex.practicum.statsdto.hit.HitDto;
import ru.yandex.practicum.statsdto.hit.StatsDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class StatsClient {

    private final RestTemplate rest;

    public StatsClient(@Value("${ewm-stat-server.url}") String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public ResponseEntity<HitDto> createHit(CreatedHitDto createdHitDto) {
        return rest.postForEntity("/hit", createdHitDto, HitDto.class);
    }

    public ResponseEntity<List<StatsDto>> getStats(String start, String end, String[] uris, Boolean unique) {
        StringBuilder pathBuilder = new StringBuilder("/stats?start={start}&end={end}&unique={unique}");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start);
        parameters.put("end", end);
        parameters.put("unique", unique);

        if (uris != null) {
            parameters.put("uris", uris);
            pathBuilder.append("&uris={uris}");
        }

        return rest.exchange(
                pathBuilder.toString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                },
                parameters
        );
    }
}
