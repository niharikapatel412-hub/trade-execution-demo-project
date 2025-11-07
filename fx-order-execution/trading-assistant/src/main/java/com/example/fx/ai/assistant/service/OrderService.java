package com.example.fx.ai.assistant.service;

import com.example.fx.ai.assistant.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service to interact with the existing order-management and price-streaming modules.
 * This acts as an integration layer between Spring AI and your existing services.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final WebClient.Builder webClientBuilder;

    @Value("${order.management.url}")
    private String orderManagementUrl;

    @Value("${price.streaming.url}")
    private String priceStreamingUrl;

    // In-memory cache for demo purposes
    private final List<Order> orderCache = Collections.synchronizedList(new ArrayList<>());

    /**
     * Get all orders from the order management system
     */
    public List<Order> getAllOrders() {
        try {
            WebClient client = webClientBuilder.baseUrl(orderManagementUrl).build();
            
            // If your order-management has a GET endpoint for orders, use it
            // Otherwise, return from cache
            Order[] orders = client.get()
                .uri("/all")
                .retrieve()
                .bodyToMono(Order[].class)
                .block();
            
            return orders != null ? Arrays.asList(orders) : orderCache;
        } catch (Exception e) {
            log.warn("Could not fetch orders from order-management service, using cache", e);
            return new ArrayList<>(orderCache);
        }
    }

    /**
     * Get orders filtered by currency pair symbol
     */
    public List<Order> getOrdersBySymbol(String symbol) {
        return getAllOrders().stream()
            .filter(order -> order.getSymbol().equalsIgnoreCase(symbol))
            .collect(Collectors.toList());
    }

    /**
     * Place a new order
     */
    public Order placeOrder(String symbol, Integer quantity, Double price, String side) {
        try {
            log.info("trading assistant placing order: {} {} {} at {}", side, quantity, symbol, price);
            WebClient client = webClientBuilder.baseUrl(orderManagementUrl).build();
            
            Map<String, Object> orderRequest = new HashMap<>();
            orderRequest.put("orderId", "ORD" + System.currentTimeMillis());
            orderRequest.put("symbol", symbol);
            orderRequest.put("quantity", quantity);
            orderRequest.put("price", price != null ? price : getCurrentPrice(symbol));
            orderRequest.put("side", side.toUpperCase());
            
            Map<String, Object> response = client.post()
                .uri("/place")
                .bodyValue(orderRequest)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
            
            // Create order object
            Order order = new Order();
            order.setOrderId((String) orderRequest.get("orderId"));
            order.setSymbol(symbol);
            order.setQuantity(quantity);
            order.setPrice((Double) orderRequest.get("price"));
            order.setSide(side.toUpperCase());
            order.setStatus("PLACED");
            
            // Add to cache
            orderCache.add(order);
            
            log.info("Order placed successfully: {}", order.getOrderId());
            return order;
            
        } catch (Exception e) {
            log.error("Error placing order", e);
            throw new RuntimeException("Failed to place order: " + e.getMessage());
        }
    }

    /**
     * Get current price from price streaming service
     */
    public Double getCurrentPrice(String symbol) {
        try {
            WebClient client = webClientBuilder.baseUrl(priceStreamingUrl).build();
            
            Map<String, Object> response = client.get()
                .uri("/streaming-price/prices")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
            
            if (response != null && response.containsKey("data")) {
                return Double.parseDouble(response.get("data").toString());
            }
            
            // Default fallback price
            return 1.1234;
            
        } catch (Exception e) {
            log.warn("Could not fetch price from streaming service, using default", e);
            return 1.1234; // Default fallback
        }
    }
}