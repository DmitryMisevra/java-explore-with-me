package ru.yandex.practicum.mainservice.request.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.mainservice.request.model.RequestStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * RequestStatusUpdateDto передается при обновлении статуса заявок
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestStatusUpdateDto {

    private List<Integer> requestIds;

    @NotNull
    private RequestStatus status;
}
