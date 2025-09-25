//package com.example.fx.order.management.models;
//
//public class StopLossOrder extends Order {
//    private final double stopPrice;
//    private boolean triggered = false;
//    private boolean executed = false;
//
//    public StopLossOrder(String orderId, double quantity, double stopPrice) {
//        super(orderId, OrderType.STOP_LOSS, quantity);
//        this.stopPrice = stopPrice;
//    }
//
//    @Override
//    public void processOrder(Price marketPrice) {
//        if (!triggered) {
//            if (marketPrice.getPrice() <= stopPrice) { // Trigger condition (for sell side)
//                triggered = true;
//               System.out.println("[Triggered] Stop Loss Order " + orderId + " at stop price: " + stopPrice);
//            } else {
//               System.out.println("[Waiting] Stop Loss Order " + orderId + " - current price: " + marketPrice.getPrice());
//            }
//        }
//
//        if (triggered && !executed) {
//            executed = true;
//           System.out.println("[Executed] Stop Loss Order " + orderId + " at market price: " + marketPrice.getPrice());
//        }
//    }
//
//    @Override
//    public boolean isCompleted() {
//        return executed;
//    }
//}
