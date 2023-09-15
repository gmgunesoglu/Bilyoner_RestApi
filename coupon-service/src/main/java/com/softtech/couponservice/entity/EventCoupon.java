package com.softtech.couponservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name="event_coupon")
@Data
@RequiredArgsConstructor
@IdClass(EventCouponId.class)
public class EventCoupon {

    @Id
    @Column(name="event_id")
    private Long eventId;


    @Id
    @Column(name="coupon_id")
    private Long couponId;
}
