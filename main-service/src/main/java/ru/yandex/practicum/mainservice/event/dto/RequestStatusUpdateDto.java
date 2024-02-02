package ru.yandex.practicum.mainservice.event.dto;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * RequestStatusUpdateDto передается при обновлении статуса заявок
 */
@Data
@Builder
public class RequestStatusUpdateDto {

    private final List<Integer> requestIds;

    @NotBlank(message = "статус не может быть пустым")
    private final String status;
}
