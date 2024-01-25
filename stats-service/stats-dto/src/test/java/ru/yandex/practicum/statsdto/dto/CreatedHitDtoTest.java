package ru.yandex.practicum.statsdto.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.statsdto.hit.CreatedHitDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreatedHitDtoTest {

    private final ObjectMapper mapper;

    public CreatedHitDtoTest() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{"
                + "\"app\": \"ewm-main-service\","
                + "\"uri\": \"/events/1\","
                + "\"ip\": \"192.163.0.1\","
                + "\"timestamp\": \"2022-09-06 11:00:23\""
                + "}";

        CreatedHitDto dto = mapper.readValue(json, CreatedHitDto.class);

        assertNotNull(dto);
        assertEquals("ewm-main-service", dto.getApp());
        assertEquals("/events/1", dto.getUri());
        assertEquals("192.163.0.1", dto.getIp());
        assertEquals(LocalDateTime.of(2022, 9, 6, 11, 0, 23), dto.getTimestamp());
    }
}
