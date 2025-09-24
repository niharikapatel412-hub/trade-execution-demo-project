package com.example.order.execution.FiXSessions;


import com.example.order.execution.Listeners.Price;
import com.example.order.execution.OrderType;

import java.util.function.Consumer;

class FixOrder extends Order {
        private String symbol;
        private String orderType; // e.g. LIMIT, MARKET, STOP
        private double price;
        private String status = "NEW"; // NEW, PARTIALLY_FILLED, FILLED, CANCELED

        private Consumer<FixOrder> orderCompletionListener;

        public FixOrder(String orderId, String symbol, double quantity, String orderType, double price) {
            super(orderId, OrderType.MARKET); // Treat as generic for this example
            this.symbol = symbol;
            this.quantity = quantity;
            this.orderType = orderType;
            this.price = price;
        }

        public String getStatus() {
            return status;
        }

        public FixMessage toFixMessage() {
            FixMessage msg = new FixMessage();
            msg.setField("35", "D");  // New Order Single
            msg.setField("11", orderId); // ClOrdID
            msg.setField("55", symbol); // Symbol
            msg.setField("54", "1");    // Side 1=Buy (for example)
            msg.setField("38", String.valueOf(quantity)); // OrderQty
            msg.setField("40", orderTypeToFixOrdType(orderType)); // OrdType
            if ("LIMIT".equals(orderType)) {
                msg.setField("44", String.valueOf(price)); // Price
            }
            msg.setField("59", "0");  // TimeInForce - Day
            return msg;
        }

        private String orderTypeToFixOrdType(String orderType) {
            switch (orderType.toUpperCase()) {
                case "MARKET": return "1";
                case "LIMIT": return "2";
                case "STOP": return "3";
                case "STOP_LIMIT": return "4";
                default: return "1";
            }
        }

        // Update order status based on execution report FIX message
        public void updateStatus(FixMessage execReport) {
            String execType = execReport.getField("150"); // ExecType
            String ordStatus = execReport.getField("39"); // OrdStatus
            String lastQty = execReport.getField("32");
            String lastPx = execReport.getField("31");

            if (ordStatus != null) {
                this.status = ordStatus;
            }
            System.out.printf("[Order %s] ExecType: %s OrdStatus: %s LastQty: %s LastPx: %s%n",
                    orderId, execType, ordStatus, lastQty, lastPx);
        }

        @Override
        public void processOrder(Price marketPrice) {
            // For FIX, order status comes asynchronously, no internal price processing here.
            System.out.println("[FIX Order] Waiting for execution reports...");
        }

        @Override
        public boolean isCompleted() {
            return "2".equals(status) || "4".equals(status) || "8".equals(status); // FILLED(2), CANCELED(4), REJECTED(8)
        }

    public void onOrderCompleted() {
        if (orderCompletionListener != null) {
            orderCompletionListener.accept(this);
        }
    }

    public void setOrderCompletionListener(Consumer<FixOrder> listener) {
        this.orderCompletionListener = listener;
    }
}


    