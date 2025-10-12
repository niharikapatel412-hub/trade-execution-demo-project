package com.example.fx.price_streaming.config;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class AlphaVantageClient {

    private final AlphaConfig alphaConfig;
    private  RestTemplate restTemplate;
    private final ObjectMapper objectMapper;



    public AlphaVantageClient(AlphaConfig alphaConfig, ObjectMapper objectMapper) {
        this.alphaConfig = alphaConfig;
        this.objectMapper = objectMapper;
        this.restTemplate = alphaConfig.restTemplate();
    }

    /**
     * Fetch latest FX rate (e.g. EUR/USD)
     */


    public double getFxRate(String fromCurrency, String toCurrency) {
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(alphaConfig.getApiUrl())
                    .queryParam("function", "CURRENCY_EXCHANGE_RATE")
                    .queryParam("from_currency", fromCurrency)
                    .queryParam("to_currency", toCurrency)
                    .queryParam("apikey", alphaConfig.getApiKey())
                    .toUriString();

            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode rateNode = root.path("Realtime Currency Exchange Rate").path("5. Exchange Rate");

            if (rateNode.isMissingNode()) {
                throw new RuntimeException("Invalid Alpha Vantage response: " + response);
            }

            double rate = rateNode.asDouble();
           log.info("[AlphaVantage]  fromCurrency= {} , toCurrency={} ,rate={}", fromCurrency, toCurrency, rate);
            return rate;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch FX rate", e);
        }
    }

}
