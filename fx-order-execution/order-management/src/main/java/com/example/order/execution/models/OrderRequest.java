package com.example.order.execution.models;
public class OrderRequest {
    private String id;
    private OrderType type;
    private Double quantity;
    private Double price;
    private Double stopPrice;

    public String getId() { return id; }
    public OrderType getType() { return type; }
    public Double getQuantity() { return quantity; }
    public Double getPrice() { return price; }
    public Double getStopPrice() { return stopPrice; }
}
