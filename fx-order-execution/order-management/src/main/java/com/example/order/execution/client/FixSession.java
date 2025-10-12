package com.example.order.execution.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

@Service
@Slf4j
public class FixSession {
    private final String host;
    private final int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Thread readerThread;
    private volatile boolean running = false;
    private Consumer<String> messageListener;

    public FixSession(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            running = true;

            // Start reader thread to listen to FIX messages from downstream
            readerThread = new Thread(() -> {
                try {
                    String line;
                    while (running && (line = in.readLine()) != null) {
                        if (messageListener != null) {
                            messageListener.accept(line);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            readerThread.start();

           log.info("[FIX Session] Connected to downstream execution system");
        } catch (IOException e) {
            throw new RuntimeException("Failed to connect to FIX server", e);
        }
    }

    public void sendFixMessage(FixMessage msg) {
        String fixStr = msg.serialize();
       log.info("[Sending FIX] " + fixStr);

    }

    public void setMessageListener(Consumer<String> listener) {
        this.messageListener = listener;
    }

    public void close() {
        running = false;
        try {
            if (socket != null) socket.close();
           log.info("[FIX Session] Closed");
        } catch (IOException e) {
           log.error("[FIX Session] Error closing socket", e);
        }
    }
}

