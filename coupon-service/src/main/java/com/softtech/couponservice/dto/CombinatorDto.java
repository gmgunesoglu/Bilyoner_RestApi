package com.softtech.couponservice.dto;

import com.softtech.couponservice.entity.MatchResultType;
import com.softtech.couponservice.entity.MatchType;
import lombok.Data;

import java.util.Date;

@Data
public class CombinatorDto {

    private Long id;
    private int mbsPoint;
    private float winPoint;
    private float drawPoint;
    private float losePoint;
    private Date startDate;
    private MatchResultType bet;

    public CombinatorDto() {
    }

    public CombinatorDto(Long id, int mbsPoint, float winPoint, float drawPoint, float losePoint, Date startDate) {
        this.id = id;
        this.mbsPoint = mbsPoint;
        this.winPoint = winPoint;
        this.drawPoint = drawPoint;
        this.losePoint = losePoint;
        this.startDate = startDate;
    }
}
