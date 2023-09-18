package com.softtech.couponservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CouponHistoryDto {

    private Long id;
    private Date startDate;
    private Date finishDate;
    private float ratio;
    private double paymentAmount;
    private double wonAmount;
}
