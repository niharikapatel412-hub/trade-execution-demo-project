# FX Trading Assistant with Spring AI

This enhancement adds an AI-powered trading assistant to your FX order execution demo project using Spring AI and OpenAI.

## 🚀 What's New

A new `trading-assistant` module that provides:
- **Natural Language Interface**: Chat with your trading system using plain English
- **Intelligent Order Management**: Ask questions like "What orders do I have?" or "Buy 100 EUR/USD"
- **Position Analysis**: Get insights on your exposure and risk
- **Real-time Price Queries**: Ask about current market prices
- **Trade Recommendations**: Receive AI-powered trading suggestions

## 🏗️ Architecture

```
fx-order-execution/
├── pom.xml (Updated with Spring AI dependencies)
├── order-management/
├── price-streaming/
├── fix-server/
└── trading-assistant/ (NEW MODULE)
    ├── pom.xml
    └── src/main/java/com/example/fx/assistant/
        ├── TradingAssistantApplication.java
        ├── config/
        │   └── AIConfiguration.java
        ├── controller/
        │   └── TradingAssistantController.java
        ├── functions/
        │   └── TradingFunctions.java (Spring AI Function Calling)
        ├── model/
        │   └── Order.java
        └── service/
            └── OrderService.java
```

## 📋 Prerequisites

1. **OpenAI API Key**: Get one from https://platform.openai.com/api-keys
2. **Java 17+**
3. **Maven 3.6+**
4. **Existing modules running**: order-management, price-streaming, fix-server

## 🔧 Setup Instructions


## 📚 Resources

- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [OpenAI API Reference](https://platform.openai.com/docs/api-reference)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)


**Happy Trading! 🚀💰**