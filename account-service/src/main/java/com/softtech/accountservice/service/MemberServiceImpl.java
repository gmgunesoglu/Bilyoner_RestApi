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

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MemberRepository repository;
    private final BalanceTransactionService balanceTransactionService;
    private final JwtUtil jwtService;

    @Override
    public AccountDetailDto get(HttpServletRequest request) {

        // get token from request
        String jwt = getJwtFromRequest(request);

        // get user from request
        Member member = getMemberFromJwt(jwt);

        // create dto and return
        return createAccountDetailDto(member);
    }

    @Override
    public AccountDetailDto register(AccountCreateDto dto) {

        // check username and email
        Member member = repository.findByUserName(dto.getUserName()) ;
        if(member!=null){
            throw new GlobalRuntimeException("This username has already been used!",HttpStatus.BAD_REQUEST);
        }
        member = repository.findByEmail(dto.getEmail());
        if(member!=null){
            throw new GlobalRuntimeException("This email has already been used!",HttpStatus.BAD_REQUEST);
        }

        // create member with input dto
        member = new Member();
        member.setFirstName(dto.getFirstName());
        member.setLastName(dto.getLastName());
        member.setUserName(dto.getUserName());
        member.setPassword(passwordEncoder.encode(dto.getPassword()));
        member.setEmail(dto.getEmail());
        member.setBanned(false);
        member.setStatue(true);

        // save member to db
        repository.save(member);

        // create firs row in balance_transaction table
        balanceTransactionService.createFirstRow(member.getId());

        // create dto and return
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

        // check member and password
        Member member = repository.findByUserNameOrEmail(dto.getUserNameOrEmail(),dto.getUserNameOrEmail());
        if(member==null || !passwordEncoder.matches(dto.getPassword(),member.getPassword())){
            throw new GlobalRuntimeException("User not found!", HttpStatus.NOT_FOUND);
        }

        if(!member.isStatue()){
            throw new GlobalRuntimeException("This account has been removed!", HttpStatus.BAD_REQUEST);
        }

        // create jwt
        String jwt= jwtService.generateJwt(member.getId()+"");

        // send LoginRequest to kafka
        JwtCacheDto jwtCacheDto = new JwtCacheDto();
        jwtCacheDto.setJwt(jwt);
        jwtCacheDto.setRole("MEMBER");
        jwtCacheDto.setUserName(member.getUserName());
        String message;
        try{
            message = objectMapper.writeValueAsString(jwtCacheDto);
            kafkaTemplate.send("LoginRequest",message);
            System.out.println("JSON: "+message);
        }catch (Exception e){
            System.out.println("JSON a çevrilemedi: "+jwtCacheDto);
        }

        // return dto
        JwtDto jwtDto = new JwtDto();
        jwtDto.setJwt(jwt);
        return jwtDto;
    }

    @Override
    public String logout(HttpServletRequest request) {

        // get jwt from request
        String jwt = getJwtFromRequest(request);

        // send LogoutRequest to kafka
        kafkaTemplate.send("LogoutRequest",jwt);

        // return
        return "Logout is success.";
    }

    @Override
    public String changePassword(HttpServletRequest request, AccountChangePasswordDto dto) {

        // check repeat password
        if(!dto.getRePassword().equals(dto.getPassword())){
            throw new GlobalRuntimeException("Repeat password didn't match!",HttpStatus.BAD_REQUEST);
        }

        // get jwt from request
        String jwt = getJwtFromRequest(request);

        // get member from request
        Member member = getMemberFromJwt(jwt);

        // check password
        if(!passwordEncoder.matches(dto.getOldPassword(),member.getPassword())){
            throw new GlobalRuntimeException("Old password didn't match!",HttpStatus.BAD_REQUEST);
        }

        // change password
        member.setPassword(passwordEncoder.encode(dto.getPassword()));
        repository.save(member);

        // send LogoutRequest to kafka
        kafkaTemplate.send("LogoutRequest",jwt);

        // return
        return "Password changed. You can log in with your new password.";
    }

    @Override
    public AccountDetailDto update(HttpServletRequest request, AccountUpdateDto dto) {

        // get member from request
        Member member = getMemberFromRequest(request);

        // check firstname
        String str = dto.getFirstName();
        if(str!=null && !str.equals("")){
            if(str.length()>=3 && str.length()<=30){
                member.setFirstName(str);
            }else{
                throw new GlobalRuntimeException("First name can be min 3, max 30 characters!",HttpStatus.BAD_REQUEST);
            }
        }

        // check lastname
        str=dto.getLastName();
        if(str!=null && !str.equals("")){
            if(str.length()>=3 && str.length()<=20){
                member.setLastName(str);
            }else{
                throw new GlobalRuntimeException("Last name can be min 3, max 20 characters!",HttpStatus.BAD_REQUEST);
            }
        }

        // check email
        str= dto.getEmail();
        if(str!=null && !str.equals("")){
            if(repository.checkEmailInUse(str)!=null && !str.equals(member.getEmail())){
                throw new GlobalRuntimeException("This email has already been used!",HttpStatus.BAD_REQUEST);
            }
            if(str.length()>=8 && str.length()<=50){
                member.setEmail(str);
            }else{
                throw new GlobalRuntimeException("Email can be min 8, max 50 characters!",HttpStatus.BAD_REQUEST);
            }
        }

        // check username
        str= dto.getUserName();
        if(str!=null && !str.equals("")){
            if(repository.checkUserNameInUse(str)!=null && !str.equals(member.getUserName())){
                throw new GlobalRuntimeException("This username has already been used!",HttpStatus.BAD_REQUEST);
            }
            if(str.length()>=8 && str.length()<=14){
                member.setUserName(str);
            }else{
                throw new GlobalRuntimeException("User name can be min 8, max 14 characters!",HttpStatus.BAD_REQUEST);
            }
        }

        // update member
        repository.save(member);

        // create dto and return
        return createAccountDetailDto(member);
    }

    @Override
    public String disable(HttpServletRequest request, AccountLoginDto dto) {

        // get jwt from request
        String jwt = getJwtFromRequest(request);

        // get member from jwt
        Member member = getMemberFromJwt(jwt);

        // check username/email and password
        if(!dto.getUserNameOrEmail().equals(member.getUserName()) && !dto.getUserNameOrEmail().equals(member.getEmail())){
            throw new GlobalRuntimeException("Username or email didn't match!",HttpStatus.BAD_REQUEST);
        }
        if(!passwordEncoder.matches(dto.getPassword(),member.getPassword())){
            throw new GlobalRuntimeException("Password didn't match!",HttpStatus.BAD_REQUEST);
        }

        // disable
        member.setStatue(false);
        repository.save(member);

        // send LogoutRequest to kafka
        kafkaTemplate.send("LogoutRequest",jwt);

        // return
        return "Account is removed!";
    }

    private String getJwtFromRequest(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        return authHeader.substring(7);
    }

    private Member getMemberFromJwt(String jwt){
        Long id = jwtService.getId(jwt);
        return repository.findById(id).get();
    }

    private Member getMemberFromRequest(HttpServletRequest request){
        String jwt = request.getHeader("Authorization").substring(7);
        Long id = jwtService.getId(jwt);
        return repository.findById(id).get();
    }

    private AccountDetailDto createAccountDetailDto(Member member) {
        AccountDetailDto dto = new AccountDetailDto();
        dto.setId(member.getId());
        dto.setFirstName(member.getFirstName());
        dto.setLastName(member.getLastName());
        dto.setUserName(member.getUserName());
        dto.setEmail(member.getEmail());
        dto.setAmount(balanceTransactionService.getCurrentBalance(member.getId()));
        return dto;
    }
}
