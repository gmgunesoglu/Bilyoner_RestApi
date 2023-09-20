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

    public TeamWithMatchesDto() {
    }

    public TeamWithMatchesDto(Long id, String name, MatchType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
}
