package com.example.order.management.models;
public class Price {
    private final double value;
    public Price(double value) { this.value = value; }
    public double getValue() { return value; }
    public static Price parse(String str) { return new Price(Double.parseDouble(str)); }
}
