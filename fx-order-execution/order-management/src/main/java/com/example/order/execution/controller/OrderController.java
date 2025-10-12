package com.example.order.execution.controller;

import com.example.order.execution.client.FixOrder;
import com.example.order.execution.models.Order;
import com.example.order.execution.models.OrderRequest;
import com.example.order.execution.services.OrderFactory;
import com.example.order.execution.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest request) {
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
}
