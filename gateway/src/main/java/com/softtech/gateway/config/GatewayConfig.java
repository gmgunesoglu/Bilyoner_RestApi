package com.softtech.gateway.config;

import com.softtech.gateway.security.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Autowired
    private AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("account-service", r -> r.path("/account/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://localhost:8070"))
                .route("coupons", r -> r.path("/coupons/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://localhost:8090"))
                .build();
    }
}
