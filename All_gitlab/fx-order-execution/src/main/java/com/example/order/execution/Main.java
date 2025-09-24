package com.example.order.execution;

public class Main {
    public static void main(String[] args) {
        StreamingService streamingService = new StreamingService();
        streamingService.start();

        OrderManagementService oms = new OrderManagementService();
        SolaceSubscriber.subscribeToPriceUpdates(oms);

        // Simulate submitting an order
//        Order order = new LimitOrder("ORD123", 100, 1.1250);
//        oms.submitOrder(order);

        //Order loopOrder = new LoopOrder("ORD456", 50);
        //oms.submitOrder(loopOrder);

        Order stopLossOrder = new StopLossOrder("ORD101", 100, 1.1200);
        oms.submitOrder(stopLossOrder);

        //Order stopLimitOrder = new StopLimitOrder("ORD102", 100, 1.1200, 1.1150);
        // oms.submitOrder(stopLimitOrder);

    }
}
