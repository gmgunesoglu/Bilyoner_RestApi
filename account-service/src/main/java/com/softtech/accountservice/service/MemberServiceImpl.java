package com.softtech.accountservice.service;

import com.softtech.accountservice.dto.*;
import com.softtech.accountservice.entity.Member;
import com.softtech.accountservice.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public AccountDetailDto register(AccountCreateDto dto) {

        // create member with input dto
        Member member = new Member();
        member.setFirstName(dto.getFirstName());
        member.setLastName(dto.getLastName());
        member.setUserName(dto.getUserName());
        member.setPassword(dto.getPassword());      //hashlenecek
        member.setEmail(dto.getEmail());
        member.setBanned(false);
        member.setStatue(true);

        // save member to db
        memberRepository.save(member);

        // create return output with member
        AccountDetailDto rDto = new AccountDetailDto();
        rDto.setId(member.getId());
        rDto.setFirstName(member.getFirstName());
        rDto.setLastName(member.getLastName());
        rDto.setUserName(member.getUserName());
        rDto.setEmail(member.getEmail());
        rDto.setAmount(0);

        return rDto;
    }

    @Override
    public JwtDto login(AccountLoginDto dto) {
        return null;
    }

    @Override
    public String logout(HttpServletRequest request) {
        return null;
    }

    @Override
    public String changePassword(HttpServletRequest request, AccountChangePasswordDto dto) {
        return null;
    }

    @Override
    public AccountDetailDto update(HttpServletRequest request, AccountUpdateDto dto) {
        return null;
    }

    @Override
    public String disable(HttpServletRequest request, AccountLoginDto dto) {
        return null;
    }
}
