package com.example.fx.ai.assistant.service;

import com.example.fx.ai.assistant.model.Order;
import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ai.tool.annotation.Tool;

import java.util.List;

/**
 * Tool methods that Spring AI can call to interact with the trading system.
 * Each public method annotated with @Tool is exposed to the model.
 */
@Component
@RequiredArgsConstructor
public class TradingTools {

    private final OrderService orderService;

    @Tool(name = "getOrders", description = "Get all orders from the order management system")
    public OrderQueryResponse getOrders(OrderQueryRequest request) {
        List<Order> orders = orderService.getAllOrders();
        return new OrderQueryResponse(orders, "Retrieved " + orders.size() + " orders");
    }

    @Tool(name = "getOrdersBySymbol",
            description = "Get orders filtered by currency pair symbol (e.g., EUR/USD, GBP/USD)")
    public OrderQueryResponse getOrdersBySymbol(SymbolFilterRequest request) {
        List<Order> orders = orderService.getOrdersBySymbol(request.symbol());
        return new OrderQueryResponse(orders, "Found " + orders.size() + " orders for " + request.symbol());
    }

    @Tool(name = "placeOrder", description = "Place a new FX order in the trading system")
    public PlaceOrderResponse placeOrder(PlaceOrderRequest request) {
        Order order = orderService.placeOrder(
                request.symbol(),
                request.quantity(),
                request.price(),
                request.side()
        );
        return new PlaceOrderResponse(
                order.getOrderId(),
                "SUCCESS",
                "Order placed successfully: " + order.getOrderId()
        );
    }

    @Tool(name = "getCurrentPrice", description = "Get current streaming price for a currency pair")
    public PriceResponse getCurrentPrice(PriceRequest request) {
        Double price = orderService.getCurrentPrice(request.symbol());
        return new PriceResponse(request.symbol(), price);
    }

    @Tool(name = "calculateExposure",
            description = "Calculate total exposure (position) for a specific currency pair")
    public ExposureResponse calculateExposure(SymbolFilterRequest request) {
        List<Order> orders = orderService.getOrdersBySymbol(request.symbol());
        int buyQuantity = orders.stream()
                .filter(o -> "BUY".equalsIgnoreCase(o.getSide()))
                .mapToInt(Order::getQuantity)
                .sum();
        int sellQuantity = orders.stream()
                .filter(o -> "SELL".equalsIgnoreCase(o.getSide()))
                .mapToInt(Order::getQuantity)
                .sum();
        int netExposure = buyQuantity - sellQuantity;

        return new ExposureResponse(
                request.symbol(),
                buyQuantity,
                sellQuantity,
                netExposure,
                netExposure > 0 ? "LONG" : (netExposure < 0 ? "SHORT" : "FLAT")
        );
    }

    // ---- Request/Response records (unchanged) -------------------------------

    @JsonClassDescription("Request to query orders")
    public record OrderQueryRequest(
            @JsonProperty(required = false)
            @JsonPropertyDescription("Optional filter criteria")
            String filter
    ) {}

    public record OrderQueryResponse(
            List<Order> orders,
            String message
    ) {}

    @JsonClassDescription("Request with currency pair symbol")
    public record SymbolFilterRequest(
            @JsonProperty(required = true)
            @JsonPropertyDescription("Currency pair symbol, e.g., EUR/USD, GBP/USD")
            String symbol
    ) {}

    @JsonClassDescription("Request to place a new order")
    public record PlaceOrderRequest(
            @JsonProperty(required = true)
            @JsonPropertyDescription("Currency pair symbol, e.g., EUR/USD")
            String symbol,

            @JsonProperty(required = true)
            @JsonPropertyDescription("Order quantity (number of units)")
            Integer quantity,

            @JsonProperty(required = false)
            @JsonPropertyDescription("Limit price (optional, for market orders leave null)")
            Double price,

            @JsonProperty(required = true)
            @JsonPropertyDescription("Order side: either BUY or SELL")
            String side
    ) {}

    public record PlaceOrderResponse(
            String orderId,
            String status,
            String message
    ) {}

    @JsonClassDescription("Request for currency pair price")
    public record PriceRequest(
            @JsonProperty(required = true)
            @JsonPropertyDescription("Currency pair symbol")
            String symbol
    ) {}

    public record PriceResponse(
            String symbol,
            Double price
    ) {}

    public record ExposureResponse(
            String symbol,
            int totalBuyQuantity,
            int totalSellQuantity,
            int netExposure,
            String position
    ) {}
}
