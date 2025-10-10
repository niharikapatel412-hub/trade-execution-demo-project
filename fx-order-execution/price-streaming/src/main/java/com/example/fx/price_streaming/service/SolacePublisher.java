package com.example.fx.price_streaming.service;


import com.example.fx.price_streaming.model.Price;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// --- Solace Publisher (Mock) ---
@Component
public
class SolacePublisher {
    private static final BlockingQueue<Price> priceQueue = new LinkedBlockingQueue<>();

    public static void publishPrice(Price price) {
        priceQueue.offer(price);
    }

    public static BlockingQueue<Price> getPriceQueue() {
        return priceQueue;
    }
}
