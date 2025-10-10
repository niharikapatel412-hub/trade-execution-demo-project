package com.example.order.execution.models;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderRequest {

    @NotNull(message = "Order type cannot be null")
    private OrderType type;

    @NotNull(message = "Order ID cannot be null")
    private String id;

    @Positive(message = "Quantity must be greater than zero")
    private int quantity;

    @Positive(message = "Price must be greater than zero")
    private double price;

    private Double stopPrice; // Optional field

    // Getters and Setters
    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Double getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(Double stopPrice) {
        this.stopPrice = stopPrice;
    }
}