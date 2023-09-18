package com.softtech.couponservice.dto;

import com.softtech.couponservice.entity.MatchType;
import lombok.Data;

import java.util.List;

@Data
public class TeamWithMatchesDto {

    private Long id;
    private String name;
    private MatchType type;
    private List<EventDto> matches;
}
