package com.softtech.couponservice.service;

import com.softtech.couponservice.dto.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface CouponService {
    CouponHistoryDetailDto getPurchased(HttpServletRequest request, Long id);

    List<CouponHistoryDto> getAllPurchased(HttpServletRequest request);

    String cancel(HttpServletRequest request, CouponCancelDto dto);

    String purchase(HttpServletRequest request, List<CouponPurchaseDto> dtos);

    List<CouponDto> getAll();

    CouponDetailDto get(Long id);
}
