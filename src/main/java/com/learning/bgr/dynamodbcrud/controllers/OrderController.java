package com.learning.bgr.dynamodbcrud.controllers;

import com.learning.bgr.dynamodbcrud.models.dynamodb.Order;
import com.learning.bgr.dynamodbcrud.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("user/{userId}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final static Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(
            @PathVariable String orderId,
            @PathVariable("userId") String customerId
    ) {
        Order order = orderService.findByOrderIdAndCustomerId(orderId, customerId);
        return ResponseEntity.ok(order);
    }

    @GetMapping()
    public ResponseEntity<List<Order>> getOrders(
            @PathVariable("userId") String customerId
    ) {
        List<Order> allOrders = orderService.findAllOrders(customerId);
        return ResponseEntity.ok(allOrders);
    }

    @PostMapping()
    public ResponseEntity<Object> createOrder(
            HttpServletRequest request,
            @PathVariable("userId") String customerId,
            @RequestBody Order order
    ) {
        order.setCustomerId(customerId);
        try {
            Order savedOrder = orderService.save(order);
            return ResponseEntity.created(
                    URI.create(request.getRequestURI() + "/" + savedOrder.getOrderId())
            ).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(
            @PathVariable("userId") String customerId,
            @PathVariable String orderId
    ) {
        String deletedOrderId = orderService.deleteOrder(customerId, orderId);
        LOGGER.info("Deleted order with id: {}", deletedOrderId);
        return ResponseEntity.noContent().build();
    }
}
