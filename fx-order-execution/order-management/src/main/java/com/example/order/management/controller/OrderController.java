package com.example.order.management.controller;

import com.example.order.management.client.FixOrder;
import com.example.order.management.models.Order;
import com.example.order.management.models.OrderRequest;
import com.example.order.management.services.OrderFactory;
import com.example.order.management.services.OrderService;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@Slf4j
@CrossOrigin(origins = "*")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest request) {
        log.info("received order placement request: {}", request);
       //randomly generate order id if not provided
        ObjectIdGenerators.UUIDGenerator idGenerator = new ObjectIdGenerators.UUIDGenerator();
        if(request.getId() == null || request.getId().isEmpty()) {
            String generatedId = idGenerator.generateId(this).toString();
            request.setId(generatedId);
            log.info("Generated order ID: {}", generatedId);
        }

        Order order = OrderFactory.createOrder(
                request.getType().toString(),
                request.getId(),
                request.getQuantity(),
                request.getPrice(),
                request.getStopPrice()
        );
        orderService.submitOrder(order);
        return  new ResponseEntity("order placed successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FixOrder> getOrderStatus(@PathVariable String id) {
        FixOrder order = null;
        try {
            order = orderService.getOrderById(id);
        } catch (Exception e) {
            log.error("Error retrieving order: {}" , e.getMessage());
        }
        return order == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(order);
    }

    @GetMapping("/all")
    public ResponseEntity<FixOrder> getAllOrders() {
        FixOrder order = null;
        try {
            order = orderService.getAllOrders();
        } catch (Exception e) {
            log.error("Error retrieving order: {}" , e.getMessage());
        }
        return order == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(order);
    }
}
