package com.example.fx.order.management.controller;


import com.example.fx.order.management.models.Order;
import com.example.fx.order.management.models.OrderRequest;
import com.example.fx.order.management.services.OrderFactory;
import com.example.fx.order.management.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

        @PostMapping("/place")
        public ResponseEntity<String> placeOrder(@RequestBody OrderRequest request) {
            Order order = OrderFactory.createOrder(
                    String.valueOf(request.getType()),
                    request.getId(),
                    request.getQuantity(),
                    request.getPrice(),
                    request.getStopPrice()
            );
            System.out.println("[OrderService] Created order " + order.getOrderId() + " with status " + order.getStatus());
            orderService.submitOrder(order);
            return ResponseEntity.ok("Order placed: " + request.getId());
        }
    }


