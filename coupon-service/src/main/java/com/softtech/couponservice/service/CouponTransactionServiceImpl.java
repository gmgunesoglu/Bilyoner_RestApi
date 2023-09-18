package com.softtech.couponservice.service;

import com.softtech.couponservice.repository.CouponTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponTransactionServiceImpl implements CouponTransactionService{

    private final CouponTransactionRepository repository;
}
