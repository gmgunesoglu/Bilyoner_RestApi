package com.softtech.couponservice.dto;

import com.softtech.couponservice.entity.MatchResultType;
import com.softtech.couponservice.entity.MatchType;
import lombok.Data;

import java.util.Date;

@Data
public class EventHistoryDto {

    private Long id;
    private MatchType matchType;
    private Date startDate;
    private String homeTeam;
    private String awayTeam;
    private int mbsPoint;
    private float winPoint;
    private float drawPoint;
    private float losePoint;
    private Date endDate;
    private int scoreGoal;
    private int concedeGoal;
    private MatchResultType bet;
    private MatchResultType matchResult;

    public EventHistoryDto() {
    }

    public EventHistoryDto(Long id, MatchType matchType, Date startDate, String homeTeam, String awayTeam,
                           int mbsPoint, float winPoint, float drawPoint, float losePoint, Date endDate,
                           int scoreGoal, int concedeGoal, MatchResultType bet, MatchResultType matchResult) {
        this.id = id;
        this.matchType = matchType;
        this.startDate = startDate;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.mbsPoint = mbsPoint;
        this.winPoint = winPoint;
        this.drawPoint = drawPoint;
        this.losePoint = losePoint;
        this.endDate = endDate;
        this.scoreGoal = scoreGoal;
        this.concedeGoal = concedeGoal;
        this.bet = bet;
        this.matchResult = matchResult;
    }
}
