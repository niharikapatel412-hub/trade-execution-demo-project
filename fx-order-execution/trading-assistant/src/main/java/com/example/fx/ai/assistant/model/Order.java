package com.example.fx.ai.assistant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String orderId;
    private String symbol;
    private Integer quantity;
    private Double price;
    private String side; // BUY or SELL
    private String status;
}