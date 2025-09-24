package com.example.order.execution.FiXSessions;


import com.example.order.execution.Listeners.Price;
import com.example.order.execution.OrderType;

import java.util.function.Consumer;


abstract class Order {
    protected final String orderId;
    protected final OrderType type;
    protected double quantity;

    Consumer<Order> orderCompletionListener;

    public Order(String orderId, OrderType type) {
        this.orderId = orderId;
        this.type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public abstract void processOrder(Price marketPrice);

    public abstract boolean isCompleted();

}



