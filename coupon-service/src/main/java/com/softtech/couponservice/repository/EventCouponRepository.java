package com.softtech.couponservice.repository;

import com.softtech.couponservice.entity.EventCoupon;
import com.softtech.couponservice.entity.EventCouponId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventCouponRepository extends JpaRepository<EventCoupon, EventCouponId> {

    @Query("SELECT COUNT(ec.eventId) FROM EventCoupon ec WHERE ec.couponId = :couponId")
    int getEventCount(Long couponId);
}
