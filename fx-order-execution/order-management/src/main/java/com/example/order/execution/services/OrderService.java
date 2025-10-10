package com.example.order.execution.services;

import com.example.order.execution.client.*;
import com.example.order.execution.models.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;

@Service
@Slf4j
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
    public void startPriceSubscription() {
        SolaceSubscriber.subscribeToPriceUpdates(this, restTemplate);
    }

    /**
     * Add a new order into pending list (will trigger when price condition met)
     */
    public void submitOrder(Order order) {
        if (orders.containsKey(order.getOrderId())) {
            log.info("[OrderService] Duplicate order ID " + order.getOrderId() + " ignored.");
            return;
        }

        FixOrder fixOrder = new FixOrder(
                order.getOrderId(),
                "EUR/USD",
                order.getQuantity(),
                order.getType(),
                order.getPrice(),
                order.getStopPrice()
        );

        orders.put(order.getOrderId(), fixOrder);
        pendingOrders.add(order);
        order.markAsPending();

         log.info("[OrderService] New order %s queued (%s) price=%.5f stop=%.5f%n",
                order.getOrderId(), order.getType(), order.getPrice(), order.getStopPrice());
    }

    /**
     * Called when new market price arrives
     */
    public void processPrice(Price price) {
        for (Order order : pendingOrders) {
            executor.submit(() -> {
                if (order.getStatus() == OrderStatus.PENDING && order.shouldExecute(price)) {
                    System.out.printf("[OrderService] Triggering order %s at market %.5f%n",
                            order.getOrderId(), price.getValue());
                    executeOrder(order);
                    order.markAsSubmitted();
                    pendingOrders.remove(order);
                }
            });
        }
    }

    /**
     * Actually send FIX message downstream
     */
    private void executeOrder(Order order) {
        FixOrder fixOrder = orders.get(order.getOrderId());
        if (fixOrder == null) {
            log.error("[OrderService] Missing FIX mapping for " + order.getOrderId());
            return;
        }

        FixMessage msg = fixOrder.toFixMessage();
        fixSession.sendFixMessage(msg);

        log.info("[OrderService] Sent FIX order %s (%s)%n",
                order.getOrderId(), order.getType());
    }

    /**
     * Handles execution reports from FIX downstream
     */
    private void onFixMessageReceived(String fixMsg) {
        log.info("[FIX Msg Received] " + fixMsg);
        FixMessage msg = FixMessage.parse(fixMsg);

        if ("8".equals(msg.getMsgType())) { // Execution Report
            String clOrdId = msg.getField("11");
            FixOrder order = orders.get(clOrdId);

            if (order != null) {
                order.updateStatus(msg);

                if (order.isCompleted()) {
                    orders.remove(clOrdId);
                    order.onOrderCompleted();
                    log.info("[OrderService] Order %s completed. Status=%s%n",
                            clOrdId, order.getStatus());
                }
            } else {
                log.error("[OrderService] Received report for unknown orderId=" + clOrdId);
            }
        }
    }

    public FixOrder getOrderById(String id) throws Exception {

            FixOrder order = orders.get(id);
            if(order == null){
                log.info("[OrderService] No order found with ID " + id);
                throw new Exception("Order not found");

        }
            return order;
    }
}
