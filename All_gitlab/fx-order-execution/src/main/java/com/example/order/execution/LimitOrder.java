package com.example.order.execution;

public class LimitOrder extends Order {
    private final double limitPrice;
    private boolean executed = false;

    public LimitOrder(String orderId, double quantity, double limitPrice) {
        super(orderId, OrderType.LIMIT, quantity);
        this.limitPrice = limitPrice;
    }

    @Override
    public void processOrder(Price marketPrice) {
        if (!executed && marketPrice.getPrice() <= limitPrice) {
            executed = true;
            System.out.println("[Executed] Limit Order " + orderId + " at price: " + marketPrice.getPrice());
        } else if (!executed) {
            System.out.println("[Pending] Limit Order " + orderId);
        }
    }

    @Override
    public boolean isCompleted() {
        return executed;
    }
}

