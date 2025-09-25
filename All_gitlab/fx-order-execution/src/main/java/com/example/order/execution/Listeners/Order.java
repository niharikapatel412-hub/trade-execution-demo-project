package com.example.order.execution.Listeners;



import com.example.order.execution.OrderType;

import java.time.Instant;
import java.util.function.Consumer;

// --- Base Order Class ---
abstract class Order {
    protected final String orderId;
    protected final OrderType type;
    protected final double quantity;

    // Callback on order completion (optional)
    private Consumer<Order> orderCompletionListener;

    // Execution info
    protected boolean executed = false;
    protected double executedPrice = 0;
    protected double executedQuantity = 0;
    protected Instant executionTime;

    public Order(String orderId, OrderType type, double quantity) {
        this.orderId = orderId;
        this.type = type;
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public abstract void processOrder(Price marketPrice);

    public abstract boolean isCompleted();

    // For single-leg orders, mark executed directly (called on execution report)
    public void onExecuted(double executedPrice, double executedQuantity) {
        this.executed = true;
        this.executedPrice = executedPrice;
        this.executedQuantity = executedQuantity;
        this.executionTime = Instant.now();
       System.out.println("[Executed] Order " + orderId + " at price " + executedPrice + " quantity " + executedQuantity);
    }

    public void setOrderCompletionListener(Consumer<Order> listener) {
        this.orderCompletionListener = listener;
    }

    public void onOrderCompleted() {
        if (orderCompletionListener != null) {
            orderCompletionListener.accept(this);
        }
    }
}





