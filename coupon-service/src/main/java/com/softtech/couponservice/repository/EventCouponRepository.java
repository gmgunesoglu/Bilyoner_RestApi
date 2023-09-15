package com.softtech.couponservice.repository;

import com.softtech.couponservice.entity.EventCoupon;
import com.softtech.couponservice.entity.EventCouponId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventCouponRepository extends JpaRepository<EventCoupon, EventCouponId> {
}
