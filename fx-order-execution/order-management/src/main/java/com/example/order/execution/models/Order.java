package com.example.order.execution.models;

public abstract class Order {
    protected final String orderId;
    protected final OrderType type;
    protected double quantity;
    protected Double price;
    protected Double stopPrice;
    public OrderStatus status = OrderStatus.PENDING;

    public Order(String orderId, OrderType type, double quantity, Double price, Double stopPrice) {
        this.orderId = orderId;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.stopPrice = stopPrice;
    }

    public String getOrderId() { return orderId; }
    public OrderType getType() { return type; }
    public OrderStatus getStatus() { return status; }
    public double getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Double getStopPrice() {
        return stopPrice;
    }

    public boolean shouldExecute(Price marketPrice) {
        return switch (type) {
            case LIMIT -> marketPrice.getValue() <= price;
            case STOP_LIMIT -> marketPrice.getValue() <= stopPrice;
            default -> false;
        };
    }

    public abstract void processOrder(Price marketPrice);
    public abstract boolean isCompleted();

    public void markAsSubmitted() {
        this.status = OrderStatus.SUBMITTED;
    }


    public void markAsPending() {
        this.status = OrderStatus.PENDING;
    }

}
