package com.softtech.accountservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softtech.accountservice.dto.*;
import com.softtech.accountservice.entity.Member;
import com.softtech.accountservice.exceptionhandling.GlobalRuntimeException;
import com.softtech.accountservice.kafka.JwtCacheDto;
import com.softtech.accountservice.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MemberRepository memberRepository;

    private final JwtUtil jwtService;

    @Override
    public AccountDetailDto register(AccountCreateDto dto) {

        // create member with input dto
        Member member = new Member();
        member.setFirstName(dto.getFirstName());
        member.setLastName(dto.getLastName());
        member.setUserName(dto.getUserName());
        member.setPassword(passwordEncoder.encode(dto.getPassword()));
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

        // check member
        Member member = memberRepository.findByUserNameOrEmail(dto.getUserNameOrEmail(),dto.getUserNameOrEmail());
        if(member==null || !passwordEncoder.matches(dto.getPassword(),member.getPassword())){
            throw new GlobalRuntimeException("User not found!", HttpStatus.NOT_FOUND);
        }

        // jwt oluştur
        String jwt= jwtService.generateJwt(member.getId()+"");

        // kafkaya LoginRequest gönder
        JwtCacheDto jwtCacheDto = new JwtCacheDto();
        jwtCacheDto.setJwt(jwt);
        jwtCacheDto.setRole("MEMBER");
        jwtCacheDto.setUserName(member.getUserName());
        String str;
        try{
            str = objectMapper.writeValueAsString(jwtCacheDto);
            kafkaTemplate.send("LoginRequest",str);
            System.out.println("JSON: "+str);
        }catch (Exception e){
            System.out.println("JSON a çevrilemedi");
        }

        // return
        JwtDto jwtDto = new JwtDto();
        jwtDto.setJwt(jwt);
        return jwtDto;
    }

    @Override
    public String logout(HttpServletRequest request) {

        // request den token a ulaş
        String jwt = getJwtFromRequest(request);

        // kafkaya LogoutRequest gönder
        kafkaTemplate.send("LogoutRequest",jwt);

        // return
        return "Logout is success.";
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

    private String getJwtFromRequest(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        return authHeader.substring(7);
    }

    private Member getMemberFromToken(String jwt){
        Long id = jwtService.getId(jwt);
        return memberRepository.findById(id).get();
    }
}
