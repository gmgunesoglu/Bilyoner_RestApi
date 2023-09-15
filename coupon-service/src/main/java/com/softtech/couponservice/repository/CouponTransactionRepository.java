package com.softtech.couponservice.repository;

import com.softtech.couponservice.entity.CouponTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponTransactionRepository extends JpaRepository<CouponTransaction,Long> {
}
