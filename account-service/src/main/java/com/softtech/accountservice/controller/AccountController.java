package com.softtech.accountservice.controller;

import com.softtech.accountservice.dto.*;
import com.softtech.accountservice.service.MemberService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final MemberService memberService;


    @GetMapping("/test")
    public String test(){
        return "account service test ok";
    }

    @PostMapping("/register")
    public AccountDetailDto register(@RequestBody @Valid AccountCreateDto dto){
        return memberService.register(dto);
    }

    @PostMapping("/login")
    public JwtDto login(@RequestBody @Valid AccountLoginDto dto){
        return memberService.login(dto);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        return memberService.logout(request);
    }

    @PostMapping("/change-password")
    public String changePassword(HttpServletRequest request, @RequestBody @Valid AccountChangePasswordDto dto){
        return memberService.changePassword(request,dto);
    }

    @PutMapping
    public AccountDetailDto update(HttpServletRequest request, @RequestBody AccountUpdateDto dto){
        return memberService.update(request,dto);
    }

    @DeleteMapping
    public String disable(HttpServletRequest request, @RequestBody @Valid AccountLoginDto dto){
        return memberService.disable(request, dto);
    }
}
