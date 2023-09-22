package com.softtech.accountservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "coupon-service", url = "${application.config.coupon-controller-url}")
public interface CouponClient {

    @GetMapping("/unresolved-coupons/member/{memberId}")
    List<Long> getAllUnresolvedCouponsId(@PathVariable Long memberId);
}
