package com.example.order.execution;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// --- Solace Publisher (Mock) ---
class SolacePublisher {
    private static final BlockingQueue<Price> priceQueue = new LinkedBlockingQueue<>();

    public static void publishPrice(Price price) {
        priceQueue.offer(price);
    }

    public static BlockingQueue<Price> getPriceQueue() {
        return priceQueue;
    }
}
