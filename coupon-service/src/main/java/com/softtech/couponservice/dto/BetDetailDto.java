package com.softtech.couponservice.dto;

import com.softtech.couponservice.entity.MatchResultType;
import com.softtech.couponservice.entity.MatchType;
import lombok.Data;

import java.util.Date;

@Data
public class BetDetailDto {

    private Long id;
    private MatchType matchType;
    private Date startDate;
    private String homeTeam;
    private String awayTeam;
    private int mbsPoint;
    private float winPoint;
    private float drawPoint;
    private float losePoint;
    private MatchResultType bet;

    public BetDetailDto() {
    }

    public BetDetailDto(Long id, MatchType matchType, Date startDate, String homeTeam, String awayTeam, int mbsPoint, float winPoint, float drawPoint, float losePoint, MatchResultType bet) {
        this.id = id;
        this.matchType = matchType;
        this.startDate = startDate;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.mbsPoint = mbsPoint;
        this.winPoint = winPoint;
        this.drawPoint = drawPoint;
        this.losePoint = losePoint;
        this.bet = bet;
    }
}
