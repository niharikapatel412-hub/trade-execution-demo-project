package com.example.fx.order.management.configurations;

import com.example.fx.order.management.client.FixSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FixConfig {
    @Bean
    public FixSession fixSession() {
        return new FixSession("localhost", 9878);
    }
}

