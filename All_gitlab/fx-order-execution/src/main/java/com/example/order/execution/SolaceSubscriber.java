package com.example.order.execution;

// --- Solace Subscriber (Mock) ---
public class SolaceSubscriber {
    public static void subscribeToPriceUpdates(OrderManagementService oms) {
        new Thread(() -> {
            try {
                while (true) {
                    Price price = SolacePublisher.getPriceQueue().take();
                    oms.processPrice(price);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
