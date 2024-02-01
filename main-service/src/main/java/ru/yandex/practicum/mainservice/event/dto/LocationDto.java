package ru.yandex.practicum.mainservice.event.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    private Long id;

    @NotNull(message = "Не указана широта координат")
    private Float lat;

    @NotNull(message = "Не указана долгота координат")
    private Float lon;
}
