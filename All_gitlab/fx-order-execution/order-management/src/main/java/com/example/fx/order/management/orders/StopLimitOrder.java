package com.example.fx.order.management.orders;

import com.example.fx.order.management.models.Order;
import com.example.fx.order.management.models.OrderStatus;
import com.example.fx.order.management.models.OrderType;
import com.example.fx.order.management.models.Price;

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
