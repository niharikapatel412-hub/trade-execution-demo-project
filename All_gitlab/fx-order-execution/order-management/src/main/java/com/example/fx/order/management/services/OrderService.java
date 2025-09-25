package com.example.fx.order.management.services;

import com.example.fx.order.management.client.FixMessage;
import com.example.fx.order.management.client.FixOrder;
import com.example.fx.order.management.client.FixSession;
import com.example.fx.order.management.client.SolaceSubscriber;
import com.example.fx.order.management.models.Order;
import com.example.fx.order.management.models.OrderStatus;
import com.example.fx.order.management.models.Price;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

@Service
public class OrderService {
    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private final ConcurrentLinkedQueue<Order> pendingOrders = new ConcurrentLinkedQueue<>();
    private final ConcurrentMap<String, FixOrder> orders = new ConcurrentHashMap<>();
    private final FixSession fixSession;

    private final RestTemplate restTemplate;

    public OrderService(FixSession fixSession, RestTemplateBuilder builder) {
        this.fixSession = fixSession;
        this.restTemplate = builder.build();
        fixSession.setMessageListener(this::onFixMessageReceived);
    }

    @PostConstruct
    public void subscribeToPrices() {

        SolaceSubscriber.subscribeToPriceUpdates(this, restTemplate);
    }

    public void submitOrder(Order order) {
        pendingOrders.add(order);
    }

    public void processPrice(Price price) {
        for (Order order : pendingOrders) {
            executor.submit(() -> {
                if (!order.isCompleted() && order.getStatus() == OrderStatus.PENDING) {
                    if (order.shouldExecute(price)) {
                        System.out.println("[OrderService] Executing order " + order.getOrderId() +
                                " at price " + price.getValue());
                        executeOrder(order);
                        order.markAsSubmitted();   // prevent duplicate execution
                    }
                }
            });
        }
    }


    private void executeOrder(Order order) {
        FixOrder fixOrder = new FixOrder(
                order.getOrderId(),
                "EUR/USD",
                order.getQuantity(),
                order.getType(),
                order.getPrice(),
                order.getStopPrice()
        );

        orders.put(order.getOrderId(), fixOrder); // ✅ Track order for status updates

        fixOrder.markAsSubmitted();

        FixMessage msg = fixOrder.toFixMessage();
        fixSession.sendFixMessage(msg);

        System.out.println("[OrderService] Sent order " + order.getOrderId() + " to FIX. Waiting for execution report...");
    }


//    private void onFixMessageReceived(String fixMsg) {
//        System.out.println("[FIX Msg Received] " + fixMsg);
//        FixMessage msg = FixMessage.parse(fixMsg);
//
//        // Only handle execution reports (35=8)
//        if ("8".equals(msg.getMsgType())) {
//            String clOrdId = msg.getField("11");  // ClOrdID
//            FixOrder order = orders.get(clOrdId);
//
//            if (order != null) {
//                String ordStatus = msg.getField("39"); // OrdStatus tag
//                if (ordStatus != null) {
//                    switch (ordStatus) {
//                        case "0" -> order.getStatus().equals(OrderStatus.SUBMITTED);  // New
//                        case "1" -> order.getStatus().equals(OrderStatus.SUBMITTED);  // Partially filled
//                        case "2" -> order.getStatus().equals(OrderStatus.COMPLETED);  // Filled
//                        case "4" -> order.getStatus().equals( OrderStatus.CANCELLED);  // Cancelled
//                        case "8" -> order.getStatus().equals (OrderStatus.REJECTED);   // Rejected
//                    }
//                }
//
//                // Print debug info
//                String execType = msg.getField("150"); // ExecType
//                String lastQty  = msg.getField("32");  // LastQty
//                String lastPx   = msg.getField("31");  // LastPx
//                System.out.printf(
//                        "[Order %s] ExecType=%s Status=%s LastQty=%s LastPx=%s%n",
//                        clOrdId, execType, order.getStatus(), lastQty, lastPx
//                );
//
//                // If completed, remove from active map
//                if (order.isCompleted()) {
//                    orders.remove(order.getOrderId());
//                    order.onOrderCompleted();
//                    System.out.println("[OrderService] Order " + clOrdId + " completed. Status=" + order.getStatus());
//                }
//            }
//        }
//    }

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
                    System.out.println("[OrderService] Order " + clOrdId + " completed. Final status=" + order.getStatus());
                }
            } else {
                System.err.println("[OrderService] Execution report for unknown orderId=" + clOrdId);
            }
        }
    }



}

