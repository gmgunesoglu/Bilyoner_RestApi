package com.softtech.couponservice.controller;

import com.softtech.couponservice.dto.CreateTeamDto;
import com.softtech.couponservice.dto.TeamDto;
import com.softtech.couponservice.dto.UpdateTeamDto;
import com.softtech.couponservice.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/teams")
@RequiredArgsConstructor
public class AdminTeamController {

    private final TeamService teamService;

    @PostMapping
    public TeamDto add(@RequestBody CreateTeamDto dto){
        return teamService.add(dto);
    }

    @PutMapping("/{id}")
    public TeamDto update(@RequestBody UpdateTeamDto dto, @PathVariable Long id){
        return teamService.update(dto, id);
    }

    @DeleteMapping("/{id}")
    public String disable(@PathVariable Long id){
        return teamService.disable(id);
    }
}
