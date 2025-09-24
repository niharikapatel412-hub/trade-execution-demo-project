package com.example.order.execution.Listeners;

class SolaceSubscriber {
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
