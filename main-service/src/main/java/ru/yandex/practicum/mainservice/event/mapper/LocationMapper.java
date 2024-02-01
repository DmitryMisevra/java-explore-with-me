package ru.yandex.practicum.mainservice.event.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.mainservice.event.dto.LocationDto;
import ru.yandex.practicum.mainservice.event.model.Location;

@Component
public class LocationMapper {

    public Location locationDtoToLocation(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }

    public LocationDto locationToLocationDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}
