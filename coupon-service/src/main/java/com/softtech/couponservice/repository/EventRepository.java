package com.softtech.couponservice.repository;

import com.softtech.couponservice.dto.CombinatorDto;
import com.softtech.couponservice.dto.EventDetailDto;
import com.softtech.couponservice.dto.EventDto;
import com.softtech.couponservice.entity.Coupon;
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
            "INNER JOIN Team t2 ON t2.id=e.awayTeamId WHERE e.startDate > :theDate AND e.statue = true")
    List<EventDetailDto> getEventDetailDtoList(Date theDate);

    @Query("SELECT new com.softtech.couponservice.dto.EventDetailDto" +
            "(e.id,e.matchType,e.startDate,t1.name,t2.name,e.mbsPoint,e.winPoint,e.drawPoint,e.losePoint) FROM Event e " +
            "INNER JOIN Team t1 ON t1.id=e.homeTeamId " +
            "INNER JOIN Team t2 ON t2.id=e.awayTeamId WHERE e.id = :eventId AND e.statue = true")
    EventDetailDto getEventDetailDto(Long eventId);

    @Query("SELECT new com.softtech.couponservice.dto.CombinatorDto" +
            "(e.id,e.mbsPoint,e.winPoint,e.drawPoint,e.losePoint,e.startDate) FROM Event e " +
            "WHERE e.id != :eventId AND e.startDate > :theDate AND e.matchType != :matchType AND e.statue = true")
    List<CombinatorDto> getCombinatorDtoListWithMatchType(Long eventId, Date theDate, MatchType matchType);

    @Query("SELECT new com.softtech.couponservice.dto.EventDto" +
            "(e.id,e.startDate,t1.name,t2.name,e.mbsPoint,e.winPoint,e.drawPoint,e.losePoint) FROM Event e " +
            "INNER JOIN Team t1 ON t1.id=e.homeTeamId " +
            "INNER JOIN Team t2 ON t2.id=e.awayTeamId WHERE e.id = :eventId AND e.statue = true ORDER BY e.startDate")
    List<EventDto> getEventDto(Long eventId);

    @Query("SELECT c FROM Event e " +
            "INNER JOIN EventCoupon ec ON e.id = ec.eventId " +
            "INNER JOIN Coupon c ON ec.couponId = c.id WHERE e.id = :eventId")
    List<Coupon> getRelatingCoupons(Long eventId);
}
