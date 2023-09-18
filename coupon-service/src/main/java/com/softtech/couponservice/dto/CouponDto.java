package com.softtech.couponservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CouponDto {

    private Long id;
    private Date startDate;
    private int totalMatch;
    private float totalRatio;
}
