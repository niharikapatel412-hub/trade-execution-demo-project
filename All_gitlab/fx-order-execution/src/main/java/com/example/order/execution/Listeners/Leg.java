package com.example.order.execution.Listeners;

import java.time.Instant;
import java.util.function.Consumer;


class Leg {
    enum Side { BUY, SELL }

    private final String legId;
    private final Side side;
    private final double quantity;

    private boolean executed = false;
    private double executedPrice;
    private double executedQuantity;
    private Instant executionTime;

    // Callback for leg execution
    private Consumer<Leg> legExecutionListener;

    public Leg(String legId, Side side, double quantity) {
        this.legId = legId;
        this.side = side;
        this.quantity = quantity;
    }

    public String getLegId() {
        return legId;
    }

    public Side getSide() {
        return side;
    }

    public double getQuantity() {
        return quantity;
    }

    public boolean isExecuted() {
        return executed;
    }

    public void markExecuted(double executedPrice, double executedQuantity) {
        this.executed = true;
        this.executedPrice = executedPrice;
        this.executedQuantity = executedQuantity;
        this.executionTime = Instant.now();
        System.out.println("[Executed] Leg " + legId + " at price " + executedPrice + " quantity " + executedQuantity);

        if (legExecutionListener != null) {
            legExecutionListener.accept(this);
        }
    }

    public void setLegExecutionListener(Consumer<Leg> listener) {
        this.legExecutionListener = listener;
    }

    @Override
    public String toString() {
        return "Leg{" +
                "legId='" + legId + '\'' +
                ", side=" + side +
                ", quantity=" + quantity +
                ", executed=" + executed +
                ", executedPrice=" + executedPrice +
                ", executedQuantity=" + executedQuantity +
                ", executionTime=" + executionTime +
                '}';
    }
}

