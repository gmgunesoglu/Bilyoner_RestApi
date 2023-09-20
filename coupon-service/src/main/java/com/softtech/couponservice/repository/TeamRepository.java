package com.softtech.couponservice.repository;

import com.softtech.couponservice.dto.TeamDto;
import com.softtech.couponservice.dto.TeamWithMatchesDto;
import com.softtech.couponservice.entity.MatchType;
import com.softtech.couponservice.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("SELECT t.id FROM Team t WHERE t.name = :teamName AND t.matchType = :matchType")
    Long getId(String teamName, MatchType matchType);

    @Query("SELECT new com.softtech.couponservice.dto.TeamDto" +
            "(t.id,t.name,t.matchType) FROM Team t")
    List<TeamDto> getAllTeamDto();

    @Query("SELECT new com.softtech.couponservice.dto.TeamWithMatchesDto" +
            "(t.id,t.name,t.matchType) " +
            "FROM Team t WHERE t.id = :id")
    TeamWithMatchesDto findTeamDto(Long id);

    Team getTeamByNameAndMatchType(String name, MatchType matchType);
}
