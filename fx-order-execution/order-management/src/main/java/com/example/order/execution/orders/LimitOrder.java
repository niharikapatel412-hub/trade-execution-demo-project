package com.example.order.execution.orders;


import com.example.order.execution.models.Order;
import com.example.order.execution.models.OrderStatus;
import com.example.order.execution.models.OrderType;
import com.example.order.execution.models.Price;

public class LimitOrder extends Order {
    public LimitOrder(String orderId, double quantity, Double price) {
        super(orderId, OrderType.LIMIT, quantity, price, null);
    }


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
