package com.example.order.execution;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MockPriceServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(5050);
       System.out.println("Mock Price Server started on port 5050...");

        Socket clientSocket = serverSocket.accept();
       System.out.println("Client connected.");

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        double price = 1.1200;
        while (true) {
            price += (Math.random() - 0.5) * 0.01;  // Simulate price fluctuation
            out.println(price);
            Thread.sleep(2000);  // Send price every 2 seconds
        }
    }
}

