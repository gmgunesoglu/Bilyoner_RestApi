package com.softtech.couponservice.repository;

import com.softtech.couponservice.dto.CombinatorDto;
import com.softtech.couponservice.dto.EventDetailDto;
import com.softtech.couponservice.dto.EventDto;
import com.softtech.couponservice.entity.Event;
import com.softtech.couponservice.entity.MatchType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {

    @Query("SELECT new com.softtech.couponservice.dto.EventDetailDto" +
            "(e.id,e.matchType,e.startDate,t1.name,t2.name,e.mbsPoint,e.winPoint,e.drawPoint,e.losePoint) FROM Event e " +
            "INNER JOIN Team t1 ON t1.id=e.homeTeamId " +
            "INNER JOIN Team t2 ON t2.id=e.awayTeamId WHERE e.startDate > :theDate")
    List<EventDetailDto> getEventDetailDtoList(Date theDate);

    @Query("SELECT new com.softtech.couponservice.dto.EventDetailDto" +
            "(e.id,e.matchType,e.startDate,t1.name,t2.name,e.mbsPoint,e.winPoint,e.drawPoint,e.losePoint) FROM Event e " +
            "INNER JOIN Team t1 ON t1.id=e.homeTeamId " +
            "INNER JOIN Team t2 ON t2.id=e.awayTeamId WHERE e.id = :eventId")
    EventDetailDto getEventDetailDto(Long eventId);

    @Query("SELECT new com.softtech.couponservice.dto.CombinatorDto" +
            "(e.id,e.mbsPoint,e.winPoint,e.drawPoint,e.losePoint,e.startDate) FROM Event e " +
            "WHERE e.id != :eventId AND e.startDate > :theDate")
    List<CombinatorDto> getCombinatorDtoList(Long eventId, Date theDate);

    @Query("SELECT new com.softtech.couponservice.dto.CombinatorDto" +
            "(e.id,e.mbsPoint,e.winPoint,e.drawPoint,e.losePoint,e.startDate) FROM Event e " +
            "WHERE e.id != :eventId AND e.startDate > :theDate AND e.matchType != :matchType")
    List<CombinatorDto> getCombinatorDtoListWithMatchType(Long eventId, Date theDate, MatchType matchType);

    @Query("SELECT new com.softtech.couponservice.dto.EventDto" +
            "(e.id,e.startDate,t1.name,t2.name,e.mbsPoint,e.winPoint,e.drawPoint,e.losePoint) FROM Event e " +
            "INNER JOIN Team t1 ON t1.id=e.homeTeamId " +
            "INNER JOIN Team t2 ON t2.id=e.awayTeamId WHERE e.id = :eventId ORDER BY e.startDate")
    List<EventDto> getEventDto(Long eventId);
}
