package com.softtech.gateway.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic loginRequestTopic(){
        return TopicBuilder.name("LoginRequest").build();
    }

    @Bean NewTopic logoutRequestTopic(){
        return TopicBuilder.name("LogoutRequest").build();
    }
}
