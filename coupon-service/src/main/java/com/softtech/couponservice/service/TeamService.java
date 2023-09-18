package com.softtech.couponservice.service;

import com.softtech.couponservice.dto.CreateTeamDto;
import com.softtech.couponservice.dto.TeamDto;
import com.softtech.couponservice.dto.TeamWithMatchesDto;
import com.softtech.couponservice.dto.UpdateTeamDto;

import java.util.List;

public interface TeamService {
    List<TeamDto> getAll();
    TeamWithMatchesDto get(Long id);
    TeamDto add(CreateTeamDto dto);
    TeamDto update(UpdateTeamDto dto, Long id);
    String disable(Long id);
}
