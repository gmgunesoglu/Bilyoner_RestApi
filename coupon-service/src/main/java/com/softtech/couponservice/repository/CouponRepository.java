package com.softtech.couponservice.repository;

import com.softtech.couponservice.dto.BetDetailDto;
import com.softtech.couponservice.dto.CouponDetailDto;
import com.softtech.couponservice.dto.CouponDto;
import com.softtech.couponservice.dto.EventDetailDto;
import com.softtech.couponservice.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon,Long> {

    @Query("SELECT new com.softtech.couponservice.dto.CouponDto" +
            "(c.id,c.startDate,c.ratio) FROM Coupon c WHERE c.startDate > :date")
    List<CouponDto> getAllCouponDto(Date date);

    @Query("SELECT new com.softtech.couponservice.dto.CouponDetailDto" +
            "(c.id,c.startDate,c.ratio) FROM Coupon c WHERE c.id = :couponId")
    CouponDetailDto getCouponDetailDto(Long couponId);

    @Query("SELECT new com.softtech.couponservice.dto.BetDetailDto" +
            "(e.id,e.matchType,e.startDate,t1.name,t2.name,e.mbsPoint,e.winPoint,e.drawPoint,e.losePoint,ec.bet) FROM Coupon c " +
            "INNER JOIN EventCoupon ec ON c.id = ec.couponId " +
            "INNER JOIN Event e ON ec.eventId = e.id " +
            "INNER JOIN Team t1 ON e.homeTeamId = t1.id " +
            "INNER JOIN Team t2 ON e.awayTeamId = t2.id WHERE c.id = :couponId")
    List<BetDetailDto> getMatches(Long couponId);
}
