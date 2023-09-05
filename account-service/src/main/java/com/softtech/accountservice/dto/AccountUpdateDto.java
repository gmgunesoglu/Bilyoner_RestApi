package com.softtech.accountservice.dto;

import lombok.Data;

@Data
public class AccountUpdateDto {

    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
}
