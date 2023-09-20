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

    public EventDto() {
    }

    public EventDto(Long id, Date startDate, String homeTeam, String awayTeam, int mbsPoint, float winPoint, float drawPoint, float losePoint) {
        this.id = id;
        this.startDate = startDate;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.mbsPoint = mbsPoint;
        this.winPoint = winPoint;
        this.drawPoint = drawPoint;
        this.losePoint = losePoint;
    }
}
