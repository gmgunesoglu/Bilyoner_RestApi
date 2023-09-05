package com.softtech.accountservice.dto;

import lombok.Data;

@Data
public class AccountDetailDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private double amount;
}
