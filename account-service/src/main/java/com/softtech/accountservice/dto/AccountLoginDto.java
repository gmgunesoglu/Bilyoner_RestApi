package com.softtech.accountservice.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountLoginDto {

    @Size(min=3, max=50, message="User nae or email can be min 3, max 50 character!")
    private String userNameOrEmail;
    @Size(min=8, max=14, message="Password can be min 8, max 14 character!")
    private String password;
}
