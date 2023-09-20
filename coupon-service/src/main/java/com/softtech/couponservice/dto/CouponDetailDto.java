package com.softtech.couponservice.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CouponDetailDto {

    private Long id;
    private Date startDate;
    private int totalMatch;
    private float totalRatio;
    private List<BetDetailDto> matches;

    public CouponDetailDto() {
    }

    public CouponDetailDto(Long id, Date startDate, float totalRatio) {
        this.id = id;
        this.startDate = startDate;
        this.totalRatio = totalRatio;
    }
}
