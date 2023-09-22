package com.softtech.couponservice.repository;

import com.softtech.couponservice.dto.*;
import com.softtech.couponservice.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon,Long> {

    @Query("SELECT new com.softtech.couponservice.dto.CouponDto" +
            "(c.id,c.startDate,c.ratio) FROM Coupon c WHERE c.startDate > :date AND c.payAmount = 0")
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

    @Query("SELECT new com.softtech.couponservice.dto.CouponHistoryDto" +
            "(c.id,c.startDate,c.endDate,c.ratio,c.payAmount) FROM Coupon c " +
            "INNER JOIN CouponTransaction ct ON c.id = ct.couponId WHERE c.memberId = :memberId")
    List<CouponHistoryDto> getAllCouponHistoryDto(Long memberId);

    @Query("SELECT new com.softtech.couponservice.dto.CouponHistoryDetailDto" +
            "(c.id,c.startDate,c.endDate,c.ratio,c.payAmount) FROM Coupon c WHERE c.memberId = :memberId AND c.id = :id")
    CouponHistoryDetailDto getCouponHistoryDetailDto(Long memberId, Long id);

    @Query("SELECT new com.softtech.couponservice.dto.EventHistoryDto" +
            "(e.id,e.matchType,e.startDate,t1.name,t2.name,e.mbsPoint,e.winPoint,e.drawPoint," +
            "e.losePoint,e.endDate,e.scoreGoal,e.concedeGoal,ec.bet,e.matchResult) " +
            "FROM Coupon c " +
            "INNER JOIN EventCoupon ec ON c.id = ec.couponId " +
            "INNER JOIN Event e ON ec.eventId = e.id " +
            "INNER JOIN Team t1 ON e.homeTeamId = t1.id " +
            "INNER JOIN Team t2 ON e.awayTeamId = t2.id WHERE c.id = :id")
    List<EventHistoryDto> getAllEventHistoryDto(Long id);

    @Query("SELECT c.id FROM Coupon c WHERE c.memberId = :memberId AND c.endDate = null")
    List<Long> getAllUnresolvedCouponsId(Long memberId);
}
