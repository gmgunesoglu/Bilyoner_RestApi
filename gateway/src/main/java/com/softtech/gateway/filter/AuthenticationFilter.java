package com.softtech.gateway.filter;


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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {

    private static final Map<String, List<String>> endPoints = new HashMap<>();
    @Autowired
    private CurrentTokens currentTokens;

    @Autowired
    public AuthenticationFilter() {
        // account service end points
        endPoints.put("/account/test", Arrays.asList("VISITOR","MEMBER","ADMIN"));  //silinecek
        endPoints.put("/account/register", Arrays.asList("VISITOR","MEMBER","ADMIN"));
        endPoints.put("/account/login", Arrays.asList("VISITOR","MEMBER","ADMIN"));
        endPoints.put("/account/logout", Arrays.asList("MEMBER","ADMIN"));
        endPoints.put("/account/change-password", Arrays.asList("MEMBER"));
        endPoints.put("/account", Arrays.asList("MEMBER","ADMIN"));
        endPoints.put("/balance/update", Arrays.asList("MEMBER","ADMIN"));

        // coupon service
        endPoints.put("/coupons/test", Arrays.asList("VISITOR","MEMBER","ADMIN"));  //silinecek
        endPoints.put("/events", Arrays.asList("VISITOR","MEMBER","ADMIN"));
        endPoints.put("/coupons", Arrays.asList("VISITOR","MEMBER","ADMIN"));
        endPoints.put("/coupons/buy", Arrays.asList("MEMBER"));
        endPoints.put("/coupons/cancel", Arrays.asList("MEMBER"));
        endPoints.put("/coupons/history", Arrays.asList("MEMBER"));
        endPoints.put("/teams", Arrays.asList("VISITOR","MEMBER","ADMIN"));
        endPoints.put("/admin/events", Arrays.asList("ADMIN"));
        endPoints.put("/admin/teams", Arrays.asList("ADMIN"));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // request path i yanlış ise not found dönsün
        String url = getUrlWithoutPathVariable(request.getURI().getPath());
        List<String> requiredRoles = endPoints.get(url);
        if(requiredRoles==null){
            return onError(exchange, HttpStatus.NOT_FOUND);
        }

        // yetki gerekmiyor ise ...
        if(requiredRoles.contains("VISITOR")){
            return chain.filter(exchange);
        }

        // Token ı cache den kontrol et, varsa çek yoksa yetkisiz dön
        final String authHeader = request.getHeaders().getFirst("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }

        // bu token ı ben mi ürettiysem, kullanıcının yetkisi yeterlimi?
        final String jwtToken = authHeader.substring(7);
        final String userRole = currentTokens.getRoleOfToken(jwtToken);
        if (userRole!=null && isSecured(request,userRole)) {
            // yetkisi var
            return chain.filter(exchange);
        }else{
            // yetkisi yok
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean isSecured(ServerHttpRequest request, String userRole){
        String path = request.getURI().getPath();
        List<String> allowedRoles = endPoints.get(path);
        return allowedRoles.contains(userRole);
    }

    private String getUrlWithoutPathVariable(String url){
        int index = url.length()-1;
        char array[] = url.toCharArray();
        if(array[index]>57 || array[index]<48){
            return url;
        }
        index--;
        while(array[index]<58 && array[index]>47){
            index--;

        }
        return url.substring(0,index);
    }
}
