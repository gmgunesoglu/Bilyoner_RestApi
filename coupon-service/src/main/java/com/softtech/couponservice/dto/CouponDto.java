package com.softtech.couponservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CouponDto {

    private Long id;
    private Date startDate;
    private int totalMatch;
    private float totalRatio;

    public CouponDto() {
    }

    public CouponDto(Long id, Date startDate, float totalRatio) {
        this.id = id;
        this.startDate = startDate;
        this.totalRatio = totalRatio;
    }
}
