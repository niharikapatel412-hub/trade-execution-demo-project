# FX Order Execution - Multi-Module Maven Project

## Overview
This project is a **multi-module Maven project** built using **Java** and **Spring Boot**.
It consists of three modules, each serving a specific purpose in the FX order execution system:

1. **`fix-server`**: A FIX protocol server that listens for incoming FIX messages.
2. **`price-streaming`**: A streaming service for publishing price updates.
3. **`order-management`**: A service for managing and processing orders.

## Project Structure
```
fx-order-execution/
├── pom.xml                     (Parent POM)
├── order-management/           (Order Management Module)
│   ├── pom.xml
│   └── src/main/java/com/example/fx/order/management/...
├── price-streaming/            (Price Streaming Module)
│   ├── pom.xml
│   └── src/main/java/com/example/fx/price_streaming/...
└── fix-server/                 (FIX Server Module)
    ├── pom.xml
    └── src/main/java/com/example/fx/fix/server/...
```

## Prerequisites
- **Java 17** or higher
- **Maven 3.6+**

## Build Instructions
To build the project, navigate to the root directory and run:

```bash
mvn clean install
```

This will compile and package all modules.

## Run Sequence
The modules must be started in the following order:

1. **Run the FIX Server**:
   Navigate to the `fix-server` module and start the application:

   ```bash
   cd fix-server
   mvn spring-boot:run
   ```

   The FIX server will start listening on port `9878`.

2. **Run the Price Streaming Server**:
   Navigate to the `price-streaming` module and start the application:

   ```bash
   cd ../price-streaming
   mvn spring-boot:run
   ```

   The price streaming service will start.

3. **Run the Order Management Server**:
   Navigate to the `order-management` module and start the application:

   ```bash
   cd ../order-management
   mvn spring-boot:run
   ```

   The order management service will start.

## Basic APIs
Here are some basic APIs you can use to interact with the system:

### Order Management APIs
1. **Place an Order**  
   **POST** `/api/orders`  
   **Request Body**:
   ```json
   {
       "orderId": "ORD123",
       "symbol": "EUR/USD",
       "quantity": 100,
       "price": 1.1234,
       "side": "BUY"
   }
   ```
   **Response**:
   ```json
   {
       "status": "SUCCESS",
       "message": "Order placed successfully"
   }
   ```

## Notes
- Ensure all modules are running in the correct sequence to avoid errors.
- The FIX server handles incoming FIX messages and sends execution reports.
- The price streaming service publishes real-time price updates.
