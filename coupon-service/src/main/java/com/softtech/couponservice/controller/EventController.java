package com.softtech.couponservice.controller;

import com.softtech.couponservice.dto.EventDetailDto;
import com.softtech.couponservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/{id}")
    public EventDetailDto get(@PathVariable Long id){
        return eventService.get(id);
    }

    @GetMapping
    public List<EventDetailDto> getAll(){
        return eventService.getAll();
    }
}
