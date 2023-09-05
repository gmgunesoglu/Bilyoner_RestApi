package com.softtech.accountservice.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountCreateDto {

    @Size(min=3, max=30, message="First name can be min 3, max 30 characters!")
    private String firstName;
    @Size(min=3, max=20, message="Last name can be min 3, max 20 characters!")
    private String lastName;
    @Size(min=8, max=14, message="User name can be min 8, max 14 characters!")
    private String userName;
    @Size(min=8, max=14, message="Password can be min 8, max 14 characters!")
    private String password;
    @Size(min=8, max=14, message="Repeat password can be min 8, max 14 characters!")
    private String rePassword;
    @Size(min=8, max=50, message="Email can be min 8, max 50 characters!")
    private String email;
}
