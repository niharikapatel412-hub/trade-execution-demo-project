package com.example.fx.order.management.client;

import com.example.fx.order.management.models.Price;
import com.example.fx.order.management.services.OrderService;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SolaceSubscriber {
//    public static void subscribeToPriceUpdates(OrderService oms) {
//        new Thread(() -> {
//            try {
//                while (true) {
//                 //   Price price = SolcePublisher.getPriceQueue().take();
//                    oms.processPrice(price);
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }



    public static void subscribeToPriceUpdates(OrderService oms, RestTemplate restTemplate) {
        new Thread(() -> {
            try {
                while (true) {
                    // Call Price microservice REST API
                    Double latestPrice = restTemplate.getForObject(
                            "http://localhost:8083/streaming-price/prices/latest",
                            Double.class
                    );

                    if (latestPrice != null) {
                        oms.processPrice(new Price(latestPrice));
                        System.out.println("[OrderService] Processed price: " + latestPrice);
                    }

                    Thread.sleep(2000); // Poll every 2 seconds
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
