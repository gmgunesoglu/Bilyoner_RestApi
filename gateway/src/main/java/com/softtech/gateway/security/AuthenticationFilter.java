package com.softtech.gateway.security;

import com.softtech.gateway.services.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {

    private static final Map<String, List<String>> endPoints = new HashMap<>();
    private JwtUtils jwtUtils;
    @Autowired
    public AuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
        // account service end points
        endPoints.put("/account/test", Arrays.asList("VISITOR","MEMBER","ADMIN"));
        endPoints.put("/account/register", Arrays.asList("VISITOR","MEMBER","ADMIN"));
        endPoints.put("/account/login", Arrays.asList("VISITOR","MEMBER","ADMIN"));
        endPoints.put("/account/logout", Arrays.asList("MEMBER","ADMIN"));
        endPoints.put("/account/change-password", Arrays.asList("MEMBER","ADMIN"));
        endPoints.put("/account/", Arrays.asList("MEMBER","ADMIN"));

        endPoints.put("/coupons/test", Arrays.asList("VISITOR","MEMBER","ADMIN"));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // request path i yanlış ise not found dönsün
        List<String> requiredRoles = endPoints.get(request.getURI().getPath());
        if(requiredRoles==null){
            return onError(exchange, HttpStatus.NOT_FOUND);
        }

        // yetki gerekmiyor ise ...
        if(requiredRoles.contains("VISITOR")){
            return chain.filter(exchange);
        }

        // yetki gerekiyor token ı ve yetkiyi cache den çek.


        // gereken rol ile kullanıcının rolünü karşılaştır.
        if (isSecured.test(request)) {
            return chain.filter(exchange);
        }else{
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    public Predicate<ServerHttpRequest> isSecured =
            request -> {
                String path = request.getURI().getPath();
                List<String> allowedRoles = endPoints.get(path);

                // Kullanıcının JWT içindeki rollerine göre erişim izni kontrol edilir.
                String token = request.getHeaders().getFirst("Authorization");
                token=token.substring(7);
                Claims claims = jwtUtils.getClaims(token);
                String userRole = claims.get("role", String.class);
                System.out.println(userRole);
                return allowedRoles.contains(userRole);
            };
}
