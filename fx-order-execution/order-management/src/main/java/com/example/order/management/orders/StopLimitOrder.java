package com.example.order.management.orders;


import com.example.order.management.models.Order;
import com.example.order.management.models.OrderStatus;
import com.example.order.management.models.OrderType;
import com.example.order.management.models.Price;

public class StopLimitOrder extends Order {
    public StopLimitOrder(String orderId, double quantity, Double stopPrice, Double limitPrice) {
        super(orderId, OrderType.STOP_LIMIT, quantity, limitPrice, stopPrice);
    }

    @Override
    public void processOrder(Price marketPrice) {
        if (shouldExecute(marketPrice)) {
            markAsSubmitted();
        }
    }

    @Override
    public boolean isCompleted() {
        return status == OrderStatus.COMPLETED;
    }
}
