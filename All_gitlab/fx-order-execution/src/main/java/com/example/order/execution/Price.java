package com.example.order.execution;

// --- Price Class ---
class Price {
    private final double price;

    public Price(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public static Price parse(String priceData) {
        return new Price(Double.parseDouble(priceData));
    }
}

