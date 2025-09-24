package com.example.order.execution;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class OrderManagementService {
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    private final ConcurrentLinkedQueue<Order> pendingOrders = new ConcurrentLinkedQueue<>();

    public void submitOrder(Order order) {
        pendingOrders.add(order);
    }

    public void processPrice(Price price) {
        for (Order order : pendingOrders) {
            executor.submit(() -> {
                order.processOrder(price);
                if (order.isCompleted()) {
                    pendingOrders.remove(order);
                }
            });
        }
    }
}
