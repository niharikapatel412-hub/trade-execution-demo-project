package com.example.fx.order.management.services;

import com.example.fx.order.management.client.FixMessage;
import com.example.fx.order.management.client.FixOrder;
import com.example.fx.order.management.client.FixSession;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class OrderManagementServiceFIX {
    private final ConcurrentMap<String, FixOrder> orders = new ConcurrentHashMap<>();
    private final FixSession fixSession;

    public OrderManagementServiceFIX(FixSession fixSession) {
        this.fixSession = fixSession;
        fixSession.setMessageListener(this::onFixMessageReceived);
    }

    public void submitOrder(FixOrder order) {
        orders.put(order.getOrderId(), order);
       System.out.println("[Submitting FIX Order] " + order.getOrderId());
        fixSession.sendFixMessage(order.toFixMessage());
    }


    // Called when FIX messages (execution reports, etc) come back
    private void onFixMessageReceived(String fixMsg) {
       System.out.println("[FIX Msg Received] " + fixMsg);
        FixMessage msg = FixMessage.parse(fixMsg);
        if ("8".equals(msg.getMsgType())) { // Execution Report
            String clOrdId = msg.getField("11");
            FixOrder order = orders.get(clOrdId);
            if (order != null) {
                order.updateStatus(msg);
                if (order.isCompleted()) {
                    orders.remove(order.getOrderId());
                    order.onOrderCompleted();
                }
            }
        }
    }
}



