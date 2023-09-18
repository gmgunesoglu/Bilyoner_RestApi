package com.softtech.couponservice.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CouponHistoryDetailDto {

    private Long id;
    private Date startDate;
    private Date finishDate;
    private float ratio;
    private double paymentAmount;
    private double wonAmount;
    private List<EventHistoryDto> matches;
}
