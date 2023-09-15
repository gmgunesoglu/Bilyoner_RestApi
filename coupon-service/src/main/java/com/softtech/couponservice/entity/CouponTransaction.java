package com.softtech.couponservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "coupon_transaction")
@Data
public class CouponTransaction {

    @Id
    @SequenceGenerator(
            name = "coupon_transaction_seq",
            sequenceName = "coupon_transaction_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "coupon_transaction_seq"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "buyer_id",
            nullable = false
    )
    private Long buyerId;

//    @Column(
//            name = "coupon_id",
//            nullable = false
//    )
//    private Long couponId;

    @Column(
            name = "coupon_transaction_type"
    )
    private CouponTransactionType couponTransactionType;

    @Column(
            name = "amount"
    )
    private double amount;

    @Column(
            name = "date"
    )
    private Date date;

    @OneToOne()
    @JoinColumn(name = "coupon_id",referencedColumnName = "id")
    private Coupon coupon;
}
