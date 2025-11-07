package com.example.fx.ai.assistant.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for the Trading Assistant that uses Spring AI.
 * Provides natural language interface to interact with the trading system.
 */
@RestController
@RequestMapping("/assistant")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class TradingAssistantController {

    private final ChatClient chatClient;
    /**
     * Main chat endpoint - accepts natural language queries about trading
     * 
     * Examples:
     * - "What orders do I have?"
     * - "Show me all EUR/USD orders"
     * - "What's my exposure to GBP/USD?"
     * - "Buy 100 EUR/USD at market price"
     * - "What's the current price of EUR/USD?"
     */
    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        log.info("Received chat request: {}", userMessage);

        try {
            String response = chatClient.prompt()
                .user(userMessage)
                .call()
                .content();

            log.info("AI Response: {}", response);
            return Map.of("response", response);
            
        } catch (Exception e) {
            log.error("Error processing chat request", e);
            return Map.of(
                "response", "I encountered an error processing your request: " + e.getMessage(),
                "error", "true"
            );
        }
    }

    /**
     * Stream response for longer conversations
     */
    @PostMapping("/chat/stream")
    public String chatStream(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        log.info("Received streaming chat request: {}", userMessage);

        return chatClient.prompt()
            .user(userMessage)
            .call()
            .content();
    }

    /**
     * Analyze trading patterns and provide insights
     */
    @PostMapping("/analyze")
    public Map<String, String> analyzeTrades(@RequestBody(required = false) Map<String, String> request) {
        String symbol = request != null ? request.get("symbol") : null;
        
        String analysisPrompt = symbol != null 
            ? "Analyze all my " + symbol + " orders. Provide insights on: " +
              "1. Trading patterns, 2. Position status, 3. Risk assessment, 4. Recommendations"
            : "Analyze all my orders across all currency pairs. Provide insights on: " +
              "1. Overall portfolio, 2. Diversification, 3. Risk exposure, 4. Recommendations";

        String response = chatClient.prompt()
            .user(analysisPrompt)
            .call()
            .content();

        return Map.of("analysis", response);
    }

    /**
     * Get trading recommendations based on current market and positions
     */
    @GetMapping("/recommend")
    public Map<String, String> getRecommendations() {
        String recommendationPrompt = 
            "Based on my current orders and positions, provide 3-5 specific trading recommendations. " +
            "Consider risk management, diversification, and current exposure.";

        String response = chatClient.prompt()
            .user(recommendationPrompt)
            .call()
            .content();

        return Map.of("recommendations", response);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
            "status", "UP",
            "service", "Trading Assistant",
            "aiProvider", "OpenAI"
        );
    }
}