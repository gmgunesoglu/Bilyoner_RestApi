package com.softtech.couponservice.controller;

import com.softtech.couponservice.dto.*;
import com.softtech.couponservice.service.CouponService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/{id}")
    public CouponDetailDto get(@PathVariable Long id){
        return couponService.get(id);
    }

    @GetMapping
    public List<CouponDto> getAll(){
        return couponService.getAll();
    }

    @PostMapping("/buy")
    public String purchase(HttpServletRequest request, @RequestBody List<CouponPurchaseDto> dtos){
        return couponService.purchase(request,dtos);
    }

    @PostMapping("/cancel")
    public String cancel(HttpServletRequest request, @RequestBody CouponCancelDto dto){
        return couponService.cancel(request,dto);
    }

    @GetMapping("/history")
    public List<CouponHistoryDto> getAllPurchased(HttpServletRequest request){
        return couponService.getAllPurchased(request);
    }

    @GetMapping("/history/{id}")
    public CouponHistoryDetailDto getPurchased(HttpServletRequest request, @PathVariable Long id){
        return couponService.getPurchased(request,id);
    }

    @GetMapping("test")
    public String test(){
        return "test";
    }

    @GetMapping("test/{id}")
    public  String testId(@PathVariable Long id){
        return "testId: "+id;
    }
}
