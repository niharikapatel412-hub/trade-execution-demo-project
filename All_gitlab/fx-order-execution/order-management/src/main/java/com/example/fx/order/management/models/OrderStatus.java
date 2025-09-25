package com.example.fx.order.management.models;

public enum OrderStatus {
    PENDING,     // Order created but not yet submitted
    SUBMITTED,   // Order sent to FIX but not completed
    COMPLETED,   // Execution report received
    CANCELLED,   // Order cancelled
    REJECTED     // Rejected by FIX server
}
