package com.example.order.management.configurations;


import com.example.order.management.client.FixSession;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class FixStartup {

    private final FixSession fixSession;

    public FixStartup(FixSession fixSession) {
        this.fixSession = fixSession;
    }

    @PostConstruct
    public void connect() {
        fixSession.connect();
    }
}
