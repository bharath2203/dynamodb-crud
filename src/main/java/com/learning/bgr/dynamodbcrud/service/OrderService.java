package com.learning.bgr.dynamodbcrud.service;

import com.learning.bgr.dynamodbcrud.models.dynamodb.Order;
import com.learning.bgr.dynamodbcrud.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order findByOrderIdAndCustomerId(String orderId, String customerId) {
        return orderRepository.findByOrderIdAndCustomerId(orderId, customerId);
    }

    public List<Order> findAllOrders(String customerId) {
        return orderRepository.findAllOrders(customerId);
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public String deleteOrder(String customerId, String orderId) {
        return orderRepository.deleteOrder(customerId, orderId);
    }
}
