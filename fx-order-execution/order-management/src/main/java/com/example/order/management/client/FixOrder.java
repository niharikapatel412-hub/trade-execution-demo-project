package com.example.order.management.client;

import com.example.order.management.models.Order;
import com.example.order.management.models.OrderStatus;
import com.example.order.management.models.OrderType;
import com.example.order.management.models.Price;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;
@Slf4j
public class FixOrder extends Order {
    private final String symbol;
    private final OrderType orderType;
    private Consumer<FixOrder> orderCompletionListener;

    public FixOrder(String orderId,
                    String symbol,
                    double quantity,
                    OrderType orderType,
                    Double price,
                    Double stopPrice) {
        super(orderId, orderType, quantity, price, stopPrice);
        this.symbol = symbol;
        this.orderType = orderType;
    }
    @Override
    public void processOrder(Price marketPrice) {
        // FIX orders wait for external execution reports instead of matching internally
        log.info("[FIX Order] Waiting for execution reports...");
    }

    @Override
    public boolean isCompleted() {
        return status == OrderStatus.COMPLETED ||
                status == OrderStatus.CANCELLED ||
                status == OrderStatus.REJECTED;
    }

    public FixMessage toFixMessage() {
        FixMessage msg = new FixMessage();
        msg.setField("35", "D");   // New Order Single
        msg.setField("11", orderId);
        msg.setField("55", symbol);
        msg.setField("54", "1");   // Side: 1 = Buy (hardcoded for now)
        msg.setField("38", String.valueOf(quantity));
        msg.setField("40", orderTypeToFixOrdType(orderType));

        if (orderType == OrderType.LIMIT || orderType == OrderType.STOP_LIMIT) {
            msg.setField("44", String.valueOf(price));
        }
        if (orderType == OrderType.STOP_LIMIT) {
            msg.setField("99", String.valueOf(stopPrice)); // Tag 99 = StopPx
        }

        msg.setField("59", "0"); // TimeInForce: 0 = Day
        return msg;
    }

    private String orderTypeToFixOrdType(OrderType type) {
        return switch (type) {
            case MARKET     -> "1";
            case LIMIT      -> "2";
            case STOP_LIMIT -> "4";
            default         -> "1";
        };
    }

    public void updateStatus(FixMessage execReport) {
        String execType = execReport.getField("150"); // ExecType
        String ordStatus = execReport.getField("39"); // OrdStatus
        String lastQty = execReport.getField("32");
        String lastPx = execReport.getField("31");

        if (ordStatus != null) {
            switch (ordStatus) {
                case "0" -> this.status = OrderStatus.SUBMITTED;  // New
                case "1" -> this.status = OrderStatus.SUBMITTED;  // Partially filled
                case "2" -> this.status = OrderStatus.COMPLETED;  // Filled
                case "4" -> this.status = OrderStatus.CANCELLED;  // Cancelled
                case "8" -> this.status = OrderStatus.REJECTED;   // Rejected
            }
        }

        System.out.printf("[Order %s] ExecType=%s Status=%s LastQty=%s LastPx=%s%n",
                orderId, execType, status, lastQty, lastPx);
    }

    public void onOrderCompleted() {
        if (orderCompletionListener != null) {
            orderCompletionListener.accept(this);
        }
    }

    public void setOrderCompletionListener(Consumer<FixOrder> listener) {
        this.orderCompletionListener = listener;
    }

    public void markAsSubmitted() {
        this.status = OrderStatus.SUBMITTED;
    }
}
