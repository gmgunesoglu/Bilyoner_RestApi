package com.softtech.couponservice.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CouponHistoryDetailDto {

    private Long id;
    private Date startDate;
    private Date endDate;
    private float ratio;
    private double paymentAmount;
    private double wonAmount;
    private List<EventHistoryDto> matches;

    public CouponHistoryDetailDto() {
    }

    public CouponHistoryDetailDto(Long id, Date startDate, Date endDate, float ratio, double paymentAmount) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ratio = ratio;
        this.paymentAmount = paymentAmount;
    }
}
