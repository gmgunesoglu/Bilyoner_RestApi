package com.softtech.couponservice.controller;

import com.softtech.couponservice.dto.TeamDto;
import com.softtech.couponservice.dto.TeamWithMatchesDto;
import com.softtech.couponservice.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public List<TeamDto> getAll(){
        return teamService.getAll();
    }

    @GetMapping("/{id}")
    public TeamWithMatchesDto get(@PathVariable Long id){
        return teamService.get(id);
    }
}
