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
    private int winPoint;
    private int drawPoint;
    private int losePoint;
    private Date endDate;
    private int scoreGoal;
    private int concedeGoal;
    private MatchResultType matchResult;
}
