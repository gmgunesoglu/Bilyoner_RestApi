package com.softtech.couponservice.controller;

import com.softtech.couponservice.dto.EventCreateDto;
import com.softtech.couponservice.dto.EventDetailDto;
import com.softtech.couponservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final EventService eventService;

    @PostMapping
    public EventDetailDto add(@RequestBody EventCreateDto dto){
        return eventService.add(dto);
    }

    @DeleteMapping("/{id}")
    public String disable(@PathVariable Long id){
        return eventService.disable(id);
    }


}
