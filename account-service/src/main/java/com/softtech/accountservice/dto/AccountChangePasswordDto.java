package com.softtech.accountservice.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountChangePasswordDto {

    @Size(min=8, max=14, message="Password can be min 8, max 14 character!")
    private String oldPassword;
    @Size(min=8, max=14, message="New password can be min 8, max 14 character!")
    private String password;
    @Size(min=8, max=14, message="Repeat password can be min 8, max 14 character!")
    private String rePassword;

}
