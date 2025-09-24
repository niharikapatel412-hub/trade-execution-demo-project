package com.example.order.execution.Listeners;


import java.util.Arrays;
import java.util.List;

// --- Main Application Class ---
public class Main {
    public static void main(String[] args) throws InterruptedException {
        StreamingService streamingService = new StreamingService();
        streamingService.start();

        OrderManagementService oms = new OrderManagementService();
        SolaceSubscriber.subscribeToPriceUpdates(oms);

//        // Sample single-leg order
//        Order limitOrder = new LimitOrder("ORD123", 100, 1.1250);
//        oms.submitOrder(limitOrder);

        // Multi-leg Loop order with callback
        List<Leg> legs = Arrays.asList(
                new Leg("LEG1", Leg.Side.BUY, 50),
                new Leg("LEG2", Leg.Side.SELL, 50),
                new Leg("LEG3", Leg.Side.BUY, 100)
        );
        LoopOrder loopOrder = new LoopOrder("ORD456", legs);

        // Register listeners for leg and order updates
        loopOrder.setLegExecutionListener(leg -> System.out.println("[Callback] Leg executed: " + leg));
        loopOrder.setOrderCompletionListener(order -> System.out.println("[Callback] LoopOrder COMPLETED: " + order.getOrderId()));

        oms.submitOrder(loopOrder);

        // Simulate execution reports arriving asynchronously
        Thread.sleep(2000); // Wait for some price updates

        oms.processExecutionReport(new ExecutionReport("ORD456", "LEG1", 1.1260, 50));
        Thread.sleep(1000);
        oms.processExecutionReport(new ExecutionReport("ORD456", "LEG2", 1.1275, 50));
        Thread.sleep(1000);
        oms.processExecutionReport(new ExecutionReport("ORD456", "LEG3", 1.1280, 100));

        Thread.sleep(3000); // Wait for all processing before exit
        System.exit(0);
    }
}