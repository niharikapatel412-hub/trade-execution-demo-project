package com.example.fx.price_streaming.controller;

import com.example.fx.price_streaming.config.AlphaVantageClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/market-data")
public class MarketDataController {

    private final AlphaVantageClient client;

    public MarketDataController(AlphaVantageClient client) {
        this.client = client;
    }

    @GetMapping("/fx/{from}/{to}")
    public double getFxRate(@PathVariable("from") String from, @PathVariable("to") String to) {

        return client.getFxRate(from, to);
    }
}
