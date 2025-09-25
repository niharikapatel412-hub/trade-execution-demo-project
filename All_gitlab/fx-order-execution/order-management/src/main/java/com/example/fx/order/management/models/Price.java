package com.example.fx.order.management.models;


public class Price {
    private final double value;

    public Price(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public static Price parse(String priceData) {
        return new Price(Double.parseDouble(priceData));
    }

    @Override
    public String toString() {
        return String.format("%.5f", value);
    }

    public double getPrice() {
        return this.value;
    }
}

