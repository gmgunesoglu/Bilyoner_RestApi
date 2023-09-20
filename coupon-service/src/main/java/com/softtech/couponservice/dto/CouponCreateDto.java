package com.softtech.couponservice.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CouponCreateDto {

    private Date startDate;
    private float ratio;
    private List<CombinatorDto> events;
}
