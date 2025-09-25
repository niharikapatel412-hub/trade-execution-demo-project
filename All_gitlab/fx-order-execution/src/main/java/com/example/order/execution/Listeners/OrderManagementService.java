package com.example.order.execution.Listeners;



import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// --- Order Management Service ---
class OrderManagementService {
    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private final ConcurrentMap<String, Order> pendingOrders = new ConcurrentHashMap<>();

    public void submitOrder(Order order) {
        pendingOrders.put(order.getOrderId(), order);
       System.out.println("[Order Submitted] " + order.getOrderId());
    }

    public void processPrice(Price price) {
        for (Order order : pendingOrders.values()) {
            executor.submit(() -> order.processOrder(price));
        }
    }

    public void processExecutionReport(ExecutionReport report) {
        Order order = pendingOrders.get(report.getOrderId());
        if (order == null) {
           System.out.println("[Warning] Execution report for unknown orderId: " + report.getOrderId());
            return;
        }

        if (order instanceof LoopOrder && report.getLegId() != null) {
            ((LoopOrder) order).onLegExecuted(report.getLegId(), report.getExecutedPrice(), report.getExecutedQuantity());
        } else {
            // For single-leg orders, mark executed directly if applicable
            if (!order.isCompleted()) {
                order.onExecuted(report.getExecutedPrice(), report.getExecutedQuantity());
            }
        }

        if (order.isCompleted()) {
            pendingOrders.remove(order.getOrderId());
           System.out.println("[Order Completed] " + order.getOrderId());
            order.onOrderCompleted();
        }
    }
}
