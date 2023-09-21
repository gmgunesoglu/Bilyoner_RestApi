package com.softtech.couponservice.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateTeamDto {

    @Size(min=3, max=30, message="Team name can be min 3, max 30 characters!")
    private String name;
}
