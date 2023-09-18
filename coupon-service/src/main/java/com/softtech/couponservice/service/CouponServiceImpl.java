package com.softtech.couponservice.service;


import com.softtech.couponservice.dto.*;
import com.softtech.couponservice.repository.CouponRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService{

    private final CouponRepository repository;

    @Override
    public CouponHistoryDetailDto getPurchased(HttpServletRequest request, Long id) {
        return null;
    }

    @Override
    public List<CouponHistoryDto> getAllPurchased(HttpServletRequest request) {
        return null;
    }

    @Override
    public String cancel(HttpServletRequest request, CouponCancelDto dto) {
        return null;
    }

    @Override
    public String purchase(HttpServletRequest request, List<CouponPurchaseDto> dtos) {
        return null;
    }

    @Override
    public List<CouponDto> getAll() {
        return null;
    }

    @Override
    public CouponDetailDto get(Long id) {
        return null;
    }
}
