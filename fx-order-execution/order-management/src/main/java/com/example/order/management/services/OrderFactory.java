package com.example.order.management.services;

import com.example.order.management.models.Order;
import com.example.order.management.models.OrderType;
import com.example.order.management.orders.LimitOrder;
import com.example.order.management.orders.MarketOrder;
import com.example.order.management.orders.StopLimitOrder;

public class OrderFactory {

    public static Order createOrder(String type, String orderId, double quantity, Double... params) {
        OrderType orderType;
        try {
            orderType = OrderType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown order type: " + type, e);
        }

        return switch (orderType) {
            case LIMIT -> {
                if (params.length < 1) {
                    throw new IllegalArgumentException("Limit Order requires a limit price.");
                }
                yield new LimitOrder(orderId, quantity, params[0]);
            }
            case STOP_LIMIT -> {
                if (params.length < 2) {
                    throw new IllegalArgumentException("Stop Limit Order requires stop price and limit price.");
                }
                yield new StopLimitOrder(orderId, quantity, params[0], params[1]);
            }
            case MARKET -> new MarketOrder(orderId, quantity);
            default -> throw new IllegalArgumentException("Unsupported order type: " + type);
        };
    }
}

