package com.example.fx.fix.server.service;


import com.example.fx.fix.server.model.Price;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// --- Solace Publisher (Mock) ---
@Component
class SolacePublisher {
    private static final BlockingQueue<Price> priceQueue = new LinkedBlockingQueue<>();

    public static void publishPrice(Price price) {
        priceQueue.offer(price);
    }

    public static BlockingQueue<Price> getPriceQueue() {
        return priceQueue;
    }
}
