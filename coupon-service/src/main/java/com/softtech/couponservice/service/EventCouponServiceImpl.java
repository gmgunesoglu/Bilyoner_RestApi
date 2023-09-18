package com.softtech.couponservice.service;

import com.softtech.couponservice.entity.EventCoupon;
import com.softtech.couponservice.repository.EventCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventCouponServiceImpl implements EventCouponService {

    private final EventCouponRepository repository;
}
