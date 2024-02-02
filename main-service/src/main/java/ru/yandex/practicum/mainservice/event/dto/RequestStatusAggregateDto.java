package ru.yandex.practicum.mainservice.event.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.mainservice.request.dto.RequestDto;

import java.util.List;

/**
 * RequestStatusAggregateDto возвращается в http ответе после обновления статуса заявок
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RequestStatusAggregateDto {

    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}
