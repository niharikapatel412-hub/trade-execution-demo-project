package com.example.order.management.orders;

import com.example.order.management.models.Order;
import com.example.order.management.models.OrderStatus;
import com.example.order.management.models.OrderType;
import com.example.order.management.models.Price;


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
