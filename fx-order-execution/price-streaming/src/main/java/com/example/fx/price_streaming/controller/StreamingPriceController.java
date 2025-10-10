package com.example.fx.price_streaming.controller;

import com.example.fx.price_streaming.model.Price;
import com.example.fx.price_streaming.service.SolacePublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.Executors;



@RestController
@RequestMapping("/streaming-price")
public class StreamingPriceController {


    /**
     * Simulating live prices using sse emitter
     * @return
     */
    @GetMapping("/prices")
    public SseEmitter streamPricesNew() {
        SseEmitter emitter = new SseEmitter();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                double price = 1.1200;
                while (true) {
                    price += (Math.random() - 0.5) * 0.01; // Simulate price fluctuation
                    emitter.send(price);
                    Thread.sleep(2000); // Send price every 2 seconds
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    @GetMapping("/prices/latest")
    public double getLatestPrice() {
        double price = 1.1200 + (Math.random() - 0.5) * 0.01;
        return price;
    }

    @Scheduled(fixedRate = 2000)
    public void publishMockPrice() {
        double price = 1.1200 + (Math.random() - 0.5) * 0.01;
        Price p = new Price(price);
        System.out.println("[Price Service] Publishing price: " + price);
        SolacePublisher.publishPrice(p);  // <-- publisher is here
    }


}
