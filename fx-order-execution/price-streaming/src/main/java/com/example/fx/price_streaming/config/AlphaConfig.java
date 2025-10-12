package com.example.fx.price_streaming.config;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AlphaConfig {

    @Value("${alphavantage.api.key}")
    private String apiKey;

    @Value("${alphavantage.api.url}")
    private String apiUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

}


