package com.learning.bgr.dynamodbcrud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class AppConfig {

    @Bean
    @Profile("default")
    public DynamoDbClient dynamoDbClient() {
        // Build AwsCredentialsProvider
        AwsCredentialsProvider credentialsProvider =
                DefaultCredentialsProvider.builder()
                        .profileName("default")
                        .build();

        // Build DynamoDbClient
        return DynamoDbClient.builder()
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Bean
    @Profile("local")
    public DynamoDbClient dynamoDbClientLocal() {

        // Build DynamoDbClient
        return DynamoDbClient.builder()
                .endpointOverride(java.net.URI.create("http://localhost:8000"))
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDBEnhancedClient() {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClientLocal())
                .build();
    }
}
