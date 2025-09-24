package com.example.order.execution.Listeners;

class ExecutionReport {
    private final String orderId;
    private final String legId;  // Nullable
    private final double executedPrice;
    private final double executedQuantity;

    public ExecutionReport(String orderId, String legId, double executedPrice, double executedQuantity) {
        this.orderId = orderId;
        this.legId = legId;
        this.executedPrice = executedPrice;
        this.executedQuantity = executedQuantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getLegId() {
        return legId;
    }

    public double getExecutedPrice() {
        return executedPrice;
    }

    public double getExecutedQuantity() {
        return executedQuantity;
    }
}
