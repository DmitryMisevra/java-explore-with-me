package ru.yandex.practicum.mainservice.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.mainservice.request.dto.RequestDto;
import ru.yandex.practicum.mainservice.request.mapper.RequestMapper;
import ru.yandex.practicum.mainservice.request.repository.RequestRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getUserRequestList(Long userId) {
        return null;
    }

    @Override
    public RequestDto createRequest(Long userId, Long eventId, RequestDto requestDto) {
        return null;
    }

    @Override
    public RequestDto cancelRequest(Long userId, Long requestId) {
        return null;
    }
}
