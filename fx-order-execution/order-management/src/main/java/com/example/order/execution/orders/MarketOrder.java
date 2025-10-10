package com.example.order.execution.orders;

import com.example.order.execution.models.Order;
import com.example.order.execution.models.OrderStatus;
import com.example.order.execution.models.OrderType;
import com.example.order.execution.models.Price;


public class MarketOrder extends Order {
    public MarketOrder(String orderId, double quantity) {
        super(orderId, OrderType.MARKET, quantity, null, null);
    }

    @Override
    public void processOrder(Price marketPrice) {
        markAsSubmitted(); // always executes immediately
    }

    @Override
    public boolean isCompleted() {
        return status == OrderStatus.COMPLETED;
    }
}
