package com.example.order.execution.FiXSessions;

public class FixMain {
    public static void main(String[] args) throws InterruptedException {
        FixSession fixSession = new FixSession("localhost", 9878);
        fixSession.connect();

        OrderManagementServiceFIX oms = new OrderManagementServiceFIX(fixSession);

        // Create a FIX order (could be market, limit, stop, etc. embedded in FIX Msg)
        FixOrder fixOrder = new FixOrder("ORD001", "EUR/USD", 100, "LIMIT", 1.1234);
        oms.submitOrder(fixOrder);

        // Register a listener to get notified when order completes
        fixOrder.setOrderCompletionListener(order ->
               System.out.println("[Order Completed] " + order.getOrderId() + " status: " + order.getStatus()));
        // Keep app running to simulate async message flow
        Thread.sleep(15000);

        fixSession.close();

        System.exit(0);
    }
}

