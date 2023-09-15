package com.softtech.couponservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "coupon")
@Data
public class Coupon {

    @Id
    @SequenceGenerator(
            name = "coupon_seq",
            sequenceName = "coupon_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "coupon_seq"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "start_date",
            nullable = false
    )
    private Date startDate;

    @Column(
            name = "ratio",
            nullable = false
    )
    private float ratio;

    @Column(
            name = "pay_amount"
    )
    private double payAmount;

    @Column(
            name = "end_date"
    )
    private Date endDate;

    @Column(
            name="statue",
            nullable = false
    )
    private boolean statue;

    @OneToMany(targetEntity = EventCoupon.class)
    @JoinColumn(name = "coupon_id",referencedColumnName = "id")
    private List<EventCoupon> eventCoupons;

    @OneToOne(mappedBy = "coupon", cascade = CascadeType.ALL)
    private CouponTransaction couponTransaction;
}
