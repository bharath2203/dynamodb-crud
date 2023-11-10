package com.learning.bgr.dynamodbcrud.repository;

import com.learning.bgr.dynamodbcrud.models.dynamodb.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final DynamoDbEnhancedClient dynamoDBEnhancedClient;

    // Saves an entity in the table
    public Order save(Order order) {
        // Generate the orderId for the order
        order.setOrderId(UUID.randomUUID().toString());
        DynamoDbTable<Order> orderTable = getTable();
        orderTable.putItem(order);
        return order;
    }

    public Order findByOrderIdAndCustomerId(String orderId, String customerId) {
        DynamoDbTable<Order> orderTable = getTable();
        // Key construction to query the table
        Key key = Key.builder()
                .sortValue(orderId)
                .partitionValue(customerId)
                .build();
        return orderTable.getItem(key);
    }

    public List<Order> findAllOrders(String customerId) {

        DynamoDbTable<Order> orderTable = getTable();

        Key key = Key.builder()
                .partitionValue(customerId)
                .build();
        QueryConditional queryConditionalToFetchAllCustomerOrders = QueryConditional.keyEqualTo(key);

        return orderTable
                .query(queryConditionalToFetchAllCustomerOrders)
                .items()
                .stream()
                .toList();
    }

    public String deleteOrder(String customerId, String orderId) {

        DynamoDbTable<Order> orderTable = getTable();

        Key key = Key.builder()
                .partitionValue(customerId)
                .sortValue(orderId)
                .build();
        DeleteItemEnhancedRequest deleteItemEnhancedRequest = DeleteItemEnhancedRequest.builder()
                .key(key)
                .build();

        orderTable.deleteItem(deleteItemEnhancedRequest);

        return orderId;
    }

    private DynamoDbTable<Order> getTable() {
        return dynamoDBEnhancedClient.table("Order", TableSchema.fromClass(Order.class));
    }
}
