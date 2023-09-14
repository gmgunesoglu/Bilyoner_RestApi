package com.softtech.accountservice.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtUtil {

    @Value("${jwt.SECRET_KEY}")
    private String SECRET_KEY;
    @Value("${jwt.TOKEN_DURATION}")
    private Long TOKEN_DURATION;

    private Key key;

    @PostConstruct
    public void initKey() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(token).getBody();
    }

    public Date getExpirationDate(String token) {
        return getClaims(token).getExpiration();
    }

    public String generateJwt(String userId) {
//        Map<String, String> claims = Map.of("id", userId, "role", role);
        Map<String, String> claims = Map.of("id", userId);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(claims.get("id"))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ TOKEN_DURATION))
                .signWith(key)
                .compact();
    }

    private boolean isExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }

    public Long getId(String jwt) {
        Claims claims = getClaims(jwt);
        return Long.parseLong((String)claims.get("id"));
    }
}
