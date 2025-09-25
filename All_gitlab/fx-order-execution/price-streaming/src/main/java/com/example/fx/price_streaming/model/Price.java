package com.example.fx.price_streaming.model;

public class Price {
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

