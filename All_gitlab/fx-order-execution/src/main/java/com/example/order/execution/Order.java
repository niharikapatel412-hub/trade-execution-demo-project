package com.example.order.execution;

// --- Base Order Class ---
abstract class Order {
    protected final String orderId;
    protected final OrderType type;
    protected final double quantity;

    public Order(String orderId, OrderType type, double quantity) {
        this.orderId = orderId;
        this.type = type;
        this.quantity = quantity;
    }

    public abstract void processOrder(Price marketPrice);

    public abstract boolean isCompleted();
}