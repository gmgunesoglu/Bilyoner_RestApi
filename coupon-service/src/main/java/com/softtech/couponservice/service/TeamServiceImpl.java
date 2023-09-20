package com.softtech.couponservice.service;

import com.softtech.couponservice.dto.CreateTeamDto;
import com.softtech.couponservice.dto.TeamDto;
import com.softtech.couponservice.dto.TeamWithMatchesDto;
import com.softtech.couponservice.dto.UpdateTeamDto;
import com.softtech.couponservice.entity.MatchType;
import com.softtech.couponservice.entity.Team;
import com.softtech.couponservice.exceptionhandling.GlobalRuntimeException;
import com.softtech.couponservice.repository.EventRepository;
import com.softtech.couponservice.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService{

    private final TeamRepository repository;
    private final EventRepository eventRepository;

    @Override
    public List<TeamDto> getAll() {
        return repository.getAllTeamDto();
    }

    @Override
    public TeamWithMatchesDto get(Long id) {
        TeamWithMatchesDto dto = repository.findTeamDto(id);
        if(dto == null){
            throw new GlobalRuntimeException("Team not found!", HttpStatus.NOT_FOUND);
        }
        dto.setMatches(eventRepository.getEventDto(dto.getId()));
        return dto;
    }

    @Override
    public TeamDto add(CreateTeamDto dto) {

        // check if not exist
        Team team = repository.getTeamByNameAndMatchType(dto.getName(),dto.getMatchType());
        if(team!=null){
            throw new GlobalRuntimeException("Team already exists!",HttpStatus.BAD_REQUEST);
        }
        // add
        team = new Team();
        team.setName(dto.getName());
        team.setMatchType(dto.getMatchType());
        team.setStatue(true);
        team = repository.save(team);

        // create return dto and return
        TeamDto rDto = new TeamDto();
        rDto.setName(team.getName());
        rDto.setId(team.getId());
        rDto.setType(team.getMatchType());
        return rDto;
    }

    @Override
    public TeamDto update(UpdateTeamDto dto, Long id) {
        return null;
    }

    @Override
    public String disable(Long id) {
        return null;
    }

    @Override
    public Long getId(String teamName, MatchType matchType) {
        return repository.getId(teamName,matchType);
    }
}
