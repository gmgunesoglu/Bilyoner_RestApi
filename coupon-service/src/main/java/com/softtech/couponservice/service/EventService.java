package com.softtech.couponservice.service;

import com.softtech.couponservice.dto.EventCreateDto;
import com.softtech.couponservice.dto.EventDetailDto;

import java.util.List;

public interface EventService {
    EventDetailDto get(Long eventId);

    List<EventDetailDto> getAll();

    EventDetailDto add(EventCreateDto dto);

    String disable(Long id);
}
