package com.softtech.couponservice.service;


import com.softtech.couponservice.dto.*;
import com.softtech.couponservice.entity.Coupon;
import com.softtech.couponservice.entity.EventCoupon;
import com.softtech.couponservice.exceptionhandling.GlobalRuntimeException;
import com.softtech.couponservice.repository.CouponRepository;
import com.softtech.couponservice.repository.EventCouponRepository;
import com.softtech.couponservice.repository.EventRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService{

    private final CouponRepository repository;
    private final EventCouponRepository eventCouponRepository;


    @Override
    public List<CouponDto> getAll() {
        Date date = new Date(System.currentTimeMillis()+300000);
        List<CouponDto> dtos = repository.getAllCouponDto(date);
        for(CouponDto dto:dtos){
            dto.setTotalMatch(eventCouponRepository.getEventCount(dto.getId()));
        }
        return dtos;
    }

    @Override
    public CouponDetailDto get(Long id) {
        CouponDetailDto dto = repository.getCouponDetailDto(id);
        if(dto==null || dto.getStartDate().before(new Date(System.currentTimeMillis()+300000))){
            throw new GlobalRuntimeException("Coupon not fpund!", HttpStatus.NOT_FOUND);
        }
        dto.setTotalMatch(eventCouponRepository.getEventCount(dto.getId()));
        dto.setMatches(repository.getMatches(dto.getId()));
        return dto;
    }

    @Override
    public String purchase(HttpServletRequest request, List<CouponPurchaseDto> dtos) {
        return null;
    }

    @Override
    public String cancel(HttpServletRequest request, CouponCancelDto dto) {
        return null;
    }

    @Override
    public List<CouponHistoryDto> getAllPurchased(HttpServletRequest request) {
        return null;
    }

    @Override
    public CouponHistoryDetailDto getPurchased(HttpServletRequest request, Long id) {
        return null;
    }

    @Override
    public void saveCoupons(EventCombinator eventCombinator){
        List<CouponCreateDto> dtos = eventCombinator.getCoupons();
        for(CouponCreateDto dto : dtos){
            Coupon coupon = new Coupon();
            coupon.setStartDate(dto.getStartDate());
            coupon.setRatio(dto.getRatio());
            coupon.setStatue(true);
            coupon = repository.save(coupon);
            for(CombinatorDto event : dto.getEvents()){
                EventCoupon eventCoupon = new EventCoupon();
                eventCoupon.setEventId(event.getId());
                eventCoupon.setCouponId(coupon.getId());
                eventCoupon.setBet(event.getBet());
                eventCouponRepository.save(eventCoupon);
            }
        }
    }

}
