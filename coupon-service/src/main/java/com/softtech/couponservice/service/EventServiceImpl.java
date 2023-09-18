package com.softtech.couponservice.service;

import com.softtech.couponservice.dto.EventCreateDto;
import com.softtech.couponservice.dto.EventDetailDto;
import com.softtech.couponservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{

    private final EventRepository repository;

    @Override
    public EventDetailDto get(Long eventId) {
        return null;
    }

    @Override
    public List<EventDetailDto> getAll() {
        return null;
    }

    @Override
    public EventDetailDto add(EventCreateDto dto) {
        return null;
    }

    @Override
    public String disable(Long id) {
        return null;
    }
}
