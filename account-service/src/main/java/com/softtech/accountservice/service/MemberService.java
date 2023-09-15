package com.softtech.accountservice.service;

import com.softtech.accountservice.dto.*;
import com.softtech.accountservice.entity.Member;
import jakarta.servlet.http.HttpServletRequest;

public interface MemberService {

    AccountDetailDto get(HttpServletRequest request);

    AccountDetailDto register(AccountCreateDto dto);

    JwtDto login(AccountLoginDto dto);

    String logout(HttpServletRequest request);

    String changePassword(HttpServletRequest request, AccountChangePasswordDto dto);

    AccountDetailDto update(HttpServletRequest request, AccountUpdateDto dto);

    String disable(HttpServletRequest request, AccountLoginDto dto);
}
