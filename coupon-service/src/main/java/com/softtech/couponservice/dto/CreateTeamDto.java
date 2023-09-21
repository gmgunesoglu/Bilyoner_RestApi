package com.softtech.couponservice.dto;

import com.softtech.couponservice.entity.MatchType;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateTeamDto {

    @Size(min=3, max=30, message="Team name can be min 3, max 30 characters!")
    private String name;
    private MatchType matchType;
}
