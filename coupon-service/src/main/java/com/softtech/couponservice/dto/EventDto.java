package com.softtech.couponservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EventDto {

    private Long id;
    private Date startDate;
    private String homeTeam;
    private String awayTeam;
    private int mbsPoint;
    private float winPoint;
    private float drawPoint;
    private float losePoint;
}
