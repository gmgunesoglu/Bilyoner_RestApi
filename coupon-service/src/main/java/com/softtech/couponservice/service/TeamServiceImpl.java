package com.softtech.couponservice.service;

import com.softtech.couponservice.dto.CreateTeamDto;
import com.softtech.couponservice.dto.TeamDto;
import com.softtech.couponservice.dto.TeamWithMatchesDto;
import com.softtech.couponservice.dto.UpdateTeamDto;
import com.softtech.couponservice.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService{

    private final TeamRepository repository;

    @Override
    public List<TeamDto> getAll() {
        return null;
    }

    @Override
    public TeamWithMatchesDto get(Long id) {
        return null;
    }

    @Override
    public TeamDto add(CreateTeamDto dto) {
        return null;
    }

    @Override
    public TeamDto update(UpdateTeamDto dto, Long id) {
        return null;
    }

    @Override
    public String disable(Long id) {
        return null;
    }
}
