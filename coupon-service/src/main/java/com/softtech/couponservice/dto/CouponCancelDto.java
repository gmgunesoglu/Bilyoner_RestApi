package com.softtech.couponservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class CouponCancelDto {

    List<Long> couponIds;
}
