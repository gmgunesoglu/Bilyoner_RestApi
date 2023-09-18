package com.softtech.couponservice.dto;

import com.softtech.couponservice.entity.MatchType;
import lombok.Data;

import java.util.Date;

@Data
public class EventDetailDto {

    private Long id;
    private MatchType matchType;
    private Date startDate;
    private String homeTeam;
    private String awayTeam;
    private int mbsPoint;
    private float winPoint;
    private float drawPoint;
    private float losePoint;
}
