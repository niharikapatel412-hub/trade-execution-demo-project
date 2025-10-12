package com.example.fx.price_streaming.controller;

import com.example.fx.price_streaming.model.Price;
import com.example.fx.price_streaming.service.SolacePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.Executors;

@RestController
@RequestMapping("/streaming-price")
@Slf4j
public class StreamingPriceController {

    @GetMapping("/prices/latest")
    public double getLatestPrice() {
        double price = 1.1200 + (Math.random() - 0.5) * 0.01;
        log.info("[Price Service] Latest price = {}", price);
        return price;
    }

    @GetMapping("/prices")
    public SseEmitter streamPrices() {
        SseEmitter emitter = new SseEmitter();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                double price = 1.1200;
                while (true) {
                    price += (Math.random() - 0.5) * 0.01;
                    emitter.send(price);
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }
}
