package com.softtech.couponservice.dto;

import com.softtech.couponservice.entity.MatchType;
import lombok.Data;

import java.util.Date;

@Data
public class EventCreateDto {

    private MatchType matchType;
    private Date startDate;
    private String homeTeam;
    private String awayTeam;
    private byte mbsPoint;
    private float winRatio;
    private float loseRatio;
}