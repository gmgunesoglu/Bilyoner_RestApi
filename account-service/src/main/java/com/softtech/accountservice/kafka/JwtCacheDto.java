package com.softtech.accountservice.kafka;

import lombok.Data;

@Data
public class JwtCacheDto {
    private String jwt;
    private String role;
    private String userName;
}
