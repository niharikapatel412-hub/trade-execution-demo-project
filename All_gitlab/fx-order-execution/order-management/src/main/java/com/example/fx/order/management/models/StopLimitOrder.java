//package com.example.fx.order.management.models;
//
//public class StopLimitOrder extends Order {
//    private final double stopPrice;
//    private final double limitPrice;
//    private boolean triggered = false;
//    private boolean executed = false;
//
//    public StopLimitOrder(String orderId, double quantity, double stopPrice, double limitPrice) {
//        super(orderId, OrderType.STOP_LOSS, quantity);
//        this.stopPrice = stopPrice;
//        this.limitPrice = limitPrice;
//    }
//
//    @Override
//    public void processOrder(Price marketPrice) {
//        if (!triggered) {
//            if (marketPrice.getPrice() <= stopPrice) { // Trigger condition (for sell side)
//                triggered = true;
//               System.out.println("[Triggered] Stop Limit Order " + orderId + " at stop price: " + stopPrice);
//            } else {
//               System.out.println("[Waiting] Stop Limit Order " + orderId + " - current price: " + marketPrice.getPrice());
//            }
//        }
//
//        if (triggered && !executed) {
//            if (marketPrice.getPrice() <= limitPrice) {
//                executed = true;
//               System.out.println("[Executed] Stop Limit Order " + orderId + " at limit price: " + marketPrice.getPrice());
//            } else {
//               System.out.println("[Waiting Execution] Stop Limit Order " + orderId + " - market price above limit price.");
//            }
//        }
//    }
//
//    @Override
//    public boolean isCompleted() {
//        return executed;
//    }
//}
//
