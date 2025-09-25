package com.example.order.execution;

class LoopOrder extends Order {
    private int executionCount = 0;
    private final int maxExecutions = 3;

    public LoopOrder(String orderId, double quantity) {
        super(orderId, OrderType.LOOP, quantity);
    }

    @Override
    public void processOrder(Price marketPrice) {
        if (executionCount < maxExecutions) {
            executionCount++;
           System.out.println("[Executed] Loop Order " + orderId + " - execution " + executionCount + " at price: " + marketPrice.getPrice());
        } else {
           System.out.println("[Completed] Loop Order " + orderId);
        }
    }

    @Override
    public boolean isCompleted() {
        return executionCount >= maxExecutions;
    }
}
