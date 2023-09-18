package com.softtech.couponservice.dto;

import com.softtech.couponservice.entity.MatchType;
import lombok.Data;

@Data
public class CreateTeamDto {

    private String name;
    private MatchType matchType;
}
