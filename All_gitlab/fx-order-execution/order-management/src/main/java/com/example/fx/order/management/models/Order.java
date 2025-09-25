package com.example.fx.order.management.models;



public abstract class Order {
    protected final String orderId;
    protected final OrderType type;

    Side side;
    protected double quantity;

    protected Double price;      // limit or market execution price
    protected Double stopPrice;  // only relevant for STOP_LIMIT
    public OrderStatus status;



    public Order(String orderId, OrderType type, double quantity, Double price, Double stopPrice) {
        this.orderId = orderId;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.stopPrice = stopPrice;
        this.status = OrderStatus.PENDING;
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderType getType() {
        return type;
    }

    public double getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Double getStopPrice() {
        return stopPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }
    public Side getSide() {return side;}

    public void setSide(Side side) {this.side = side;}

    public abstract void processOrder(Price marketPrice);

    public abstract boolean isCompleted();

    /**
     * Decide if this order should execute at the given market price.
     */
    public boolean shouldExecute(Price marketPrice) {
        return switch (type) {
            case LIMIT -> marketPrice.getValue() <= this.price;
            case STOP_LIMIT -> marketPrice.getValue() <= this.stopPrice;
            case MARKET -> true; // always execute immediately
            case STOP_LOSS -> false;
            case LOOP -> false;
            case GAMMA -> false;
        };
    }

    public void markAsSubmitted() {
        this.status = OrderStatus.SUBMITTED;
        System.out.println("[OrderService] Order " + orderId + " marked as SUBMITTED");
    }

    public void markAsCompleted() {
        this.status = OrderStatus.COMPLETED;
        System.out.println("[OrderService] Order " + orderId + " marked as COMPLETED");
    }

}
