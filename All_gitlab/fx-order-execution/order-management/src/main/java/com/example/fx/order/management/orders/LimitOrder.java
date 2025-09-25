package com.example.fx.order.management.orders;

import com.example.fx.order.management.models.Order;
import com.example.fx.order.management.models.OrderStatus;
import com.example.fx.order.management.models.OrderType;
import com.example.fx.order.management.models.Price;

public class LimitOrder extends Order {
    public LimitOrder(String orderId, double quantity, Double price) {
        super(orderId, OrderType.LIMIT, quantity, price, null);
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
