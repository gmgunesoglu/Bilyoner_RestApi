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
    private List<EventDetailDto> matches;
}
