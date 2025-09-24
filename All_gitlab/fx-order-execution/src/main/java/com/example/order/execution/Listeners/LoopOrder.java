package com.example.order.execution.Listeners;



import com.example.order.execution.OrderType;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

// --- Loop Order ---
class LoopOrder extends Order {
    private final List<Leg> legs;

    // Callbacks for leg execution and order completion
    private Consumer<Leg> legExecutionListener;
    private Consumer<Order> orderCompletionListener;

    public LoopOrder(String orderId, List<Leg> legs) {
        super(orderId, OrderType.LOOP, legs.stream().mapToDouble(Leg::getQuantity).sum());
        this.legs = new CopyOnWriteArrayList<>(legs); // thread-safe list
    }

    @Override
    public void processOrder(Price marketPrice) {
        // Just a placeholder: in a real system, this might send legs downstream
        System.out.println("[LoopOrder] " + orderId + " awaiting leg executions...");
    }

    public void onLegExecuted(String legId, double executedPrice, double executedQuantity) {
        for (Leg leg : legs) {
            if (leg.getLegId().equals(legId) && !leg.isExecuted()) {
                leg.markExecuted(executedPrice, executedQuantity);
                if (legExecutionListener != null) {
                    legExecutionListener.accept(leg);
                }
                break;
            }
        }
    }

    @Override
    public boolean isCompleted() {
        return legs.stream().allMatch(Leg::isExecuted);
    }

    public void setLegExecutionListener(Consumer<Leg> listener) {
        this.legExecutionListener = listener;
    }

    @Override
    public void setOrderCompletionListener(Consumer<Order> listener) {
        this.orderCompletionListener = listener;
    }

    @Override
    public void onOrderCompleted() {
        if (orderCompletionListener != null) {
            orderCompletionListener.accept(this);
        }
    }
}

