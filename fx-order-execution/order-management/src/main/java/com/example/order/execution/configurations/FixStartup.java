package com.example.order.execution.configurations;


import com.example.order.execution.client.FixSession;
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
