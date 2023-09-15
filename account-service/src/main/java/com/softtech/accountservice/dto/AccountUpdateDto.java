package com.softtech.accountservice.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountUpdateDto {

    private String firstName;
    private String lastName;
    private String userName;
    private String email;
}
