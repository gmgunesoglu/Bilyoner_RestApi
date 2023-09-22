package com.softtech.couponservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CouponHistoryDto {

    private Long id;
    private Date startDate;
    private Date endDate;
    private float ratio;
    private double paymentAmount;
    private double wonAmount;

    public CouponHistoryDto() {
    }

    public CouponHistoryDto(Long id, Date startDate, Date endDate, float ratio, double paymentAmount) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ratio = ratio;
        this.paymentAmount = paymentAmount;
    }
}
