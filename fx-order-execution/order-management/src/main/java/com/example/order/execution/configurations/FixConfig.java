package com.example.order.execution.configurations;


import com.example.order.execution.client.FixSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FixConfig {
    @Bean
    public FixSession fixSession() {
        return new FixSession("localhost", 9878);
    }
}

