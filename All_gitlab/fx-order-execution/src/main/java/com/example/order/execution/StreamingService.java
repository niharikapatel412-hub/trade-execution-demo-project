package com.example.order.execution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class StreamingService {

    public void start() {
        new Thread(() -> {
            try (Socket socket = new Socket("localhost", 5050);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                String priceData;
                while ((priceData = in.readLine()) != null) {
                    Price price = Price.parse(priceData);
                    SolacePublisher.publishPrice(price);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
