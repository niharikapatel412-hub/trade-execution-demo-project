package com.example.order.execution.services;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import com.example.order.execution.client.FixMessage;
import com.example.order.execution.client.FixOrder;
import com.example.order.execution.client.FixSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderManagementServiceFIX {
    private final ConcurrentMap<String, FixOrder> orders = new ConcurrentHashMap<>();
    private final FixSession fixSession;

    public OrderManagementServiceFIX(FixSession fixSession) {
        this.fixSession = fixSession;
        fixSession.setMessageListener(this::onFixMessageReceived);
    }

    public void submitOrder(FixOrder order) {
        if (orders.containsKey(order.getOrderId())) {
            log.info("[OrderService] Duplicate order ID " + order.getOrderId() + " ignored.");
            return;
        }
        orders.put(order.getOrderId(), order);
       log.info("[Submitting FIX Order] " + order.getOrderId());
        order.markAsSubmitted();
        log.info("[OrderService] Order " + order.getOrderId() + " submitted.");
       fixSession.sendFixMessage(order.toFixMessage());
    }





    // Called when FIX messages (execution reports, etc) come back
    private void onFixMessageReceived(String fixMsg) {
       log.info("[FIX Msg Received] " + fixMsg);
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



