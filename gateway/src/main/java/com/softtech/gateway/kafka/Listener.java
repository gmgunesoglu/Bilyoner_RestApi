package com.softtech.gateway.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softtech.gateway.filter.AuthenticationFilter;
import com.softtech.gateway.filter.CurrentTokens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Listener {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private CurrentTokens currentTokens;

//    @KafkaListener(
//            topics = "JwtAndRole",
//            groupId = "groupId"
//    )
//    void listenerTest(String data){
//        System.out.println("Login isteği: "+data);
//    }

    @KafkaListener(
            topics = "LoginRequest",
            groupId = "groupId"
    )
    void loginRequestListener(String data){
        try{
            JwtCacheDto jwtCacheDto = objectMapper.readValue(data, JwtCacheDto.class);
            currentTokens.addToCache(jwtCacheDto.getJwt(), jwtCacheDto.getUserName(), jwtCacheDto.getRole());
            System.out.println("Login talebi işleme alındı: "+data);
        }catch (Exception e){
            System.out.println("Login talebi başarısız: "+data);
        }
    }

    @KafkaListener(
            topics = "LogoutRequest",
            groupId = "groupId"
    )
    void logoutRequestListener(String jwt){
        currentTokens.removeFromCache(jwt);
    }
}
