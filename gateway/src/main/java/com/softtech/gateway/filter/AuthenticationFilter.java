package com.softtech.gateway.filter;

import com.softtech.gateway.exceptionhandling.GlobalRuntimeException;
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
    @Autowired
    private CurrentTokens currentTokens;

    @Autowired
    public AuthenticationFilter() {
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

    private ServerHttpResponse invalidJwt(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response;
    }

//    public Predicate<ServerHttpRequest> isSecured =
//            request -> {
//                String path = request.getURI().getPath();
//                List<String> allowedRoles = endPoints.get(path);
//
//                // Kullanıcının JWT içindeki rollerine göre erişim izni kontrol edilir.
//                String token = request.getHeaders().getFirst("Authorization");
//                token=token.substring(7);
//                Claims claims = jwtUtils.getClaims(token);
//                String userRole = claims.get("role", String.class);
//                System.out.println(userRole);
//                return allowedRoles.contains(userRole);
//            };

    private boolean isSecured(ServerHttpRequest request, String userRole){
        String path = request.getURI().getPath();
        List<String> allowedRoles = endPoints.get(path);
        return allowedRoles.contains(userRole);
    }

    public void addToCache(String token, String userName, String role){
        currentTokens.addToCache(token, userName, role);
    }

    public void removeFromCache(String token){
        currentTokens.removeFromCache(token);
    }
}
