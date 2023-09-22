package com.softtech.couponservice.repository;

import com.softtech.couponservice.entity.CouponTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CouponTransactionRepository extends JpaRepository<CouponTransaction,Long> {

    @Query("SELECT ct.amount FROM CouponTransaction ct WHERE ct.couponId = :couponId AND ct.couponTransactionType = 'COUPON_WIN'")
    Optional<Double> getWonAmount(Long couponId);
}
