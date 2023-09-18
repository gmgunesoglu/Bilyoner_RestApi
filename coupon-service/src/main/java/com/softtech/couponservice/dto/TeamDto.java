package com.softtech.couponservice.dto;

import com.softtech.couponservice.entity.MatchType;
import lombok.Data;

@Data
public class TeamDto {

    private Long id;
    private String name;
    private MatchType type;
}
