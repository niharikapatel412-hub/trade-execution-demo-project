package com.example.order.management.client;


import com.example.order.management.models.Price;
import com.example.order.management.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class SolaceSubscriber {

    private static final String apiURL = "http://localhost:8085/streaming-price/prices/latest";

    public static void subscribeToPriceUpdates(OrderService oms, RestTemplate restTemplate) {
        new Thread(() -> {
            try {
                while (true) {
                    // Call Price microservice REST API
                    Double latestPrice = restTemplate.getForObject(
                            apiURL,
                            Double.class
                    );

                    if (latestPrice != null) {
                        oms.processPrice(new Price(latestPrice));
                       // log.info("[OrderService] Processed price: " + latestPrice);
                    }

                    Thread.sleep(2000); // Poll every 2 seconds
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
