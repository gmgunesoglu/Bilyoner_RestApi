package com.softtech.couponservice.service;

import com.softtech.couponservice.dto.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface CouponService {

    List<CouponDto> getAll();
    CouponDetailDto get(Long id);
    String purchase(HttpServletRequest request, List<CouponPurchaseDto> dtos);
    String cancel(HttpServletRequest request, CouponCancelDto dto);
    List<CouponHistoryDto> getAllPurchased(HttpServletRequest request);
    CouponHistoryDetailDto getPurchased(HttpServletRequest request, Long id);

    void saveCoupons(EventCombinator eventCombinator);
}
