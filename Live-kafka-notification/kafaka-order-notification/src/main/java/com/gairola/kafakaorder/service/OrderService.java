package com.gairola.kafakaorder.service;

import com.gairola.kafakaorder.model.CreateOrderRequest;
import com.gairola.kafakaorder.model.Order;
import com.gairola.kafakaorder.model.OrderEvent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class OrderService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    private final List<Order> orders =
            new CopyOnWriteArrayList<>();

    @Value("${app.kafka.order-topic}")
    private String orderTopic;

    public OrderService(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper) {

        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public Order createOrder(CreateOrderRequest request) {

        String orderId =
                "ORD-" + System.currentTimeMillis();

        Order order =
                new Order(
                        orderId,
                        request.userId(),
                        "CONFIRMED",
                        Instant.now()
                );

        orders.add(order);

        OrderEvent event =
                new OrderEvent(
                        order.orderId(),
                        order.userId(),
                        "ORDER_CONFIRMED",
                        "Your order "
                                + order.orderId()
                                + " has been confirmed",
                        order.createdAt()
                );

        try {

            // Convert OrderEvent to JSON
            String json =
                    objectMapper.writeValueAsString(event);

            System.out.println();
            System.out.println(
                    "========================================"
            );
            System.out.println(
                    "SENDING MESSAGE TO KAFKA"
            );
            System.out.println(
                    "Topic     : " + orderTopic
            );
            System.out.println(
                    "Key       : " + order.userId()
            );
            System.out.println(
                    "Kafka JSON: " + json
            );
            System.out.println(
                    "========================================"
            );

            // Send JSON String to Kafka
            kafkaTemplate
                    .send(
                            orderTopic,
                            order.userId(),
                            json
                    )
                    .whenComplete((result, ex) -> {

                        if (ex != null) {

                            System.err.println();
                            System.err.println(
                                    "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
                            );
                            System.err.println(
                                    "KAFKA SEND FAILED"
                            );
                            System.err.println(
                                    "Topic : " + orderTopic
                            );
                            System.err.println(
                                    "Error : " + ex.getMessage()
                            );
                            System.err.println(
                                    "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
                            );

                            ex.printStackTrace();

                        } else {

                            System.out.println();
                            System.out.println(
                                    "========================================"
                            );
                            System.out.println(
                                    "KAFKA SEND SUCCESSFUL"
                            );
                            System.out.println(
                                    "Topic     : "
                                            + result
                                            .getRecordMetadata()
                                            .topic()
                            );
                            System.out.println(
                                    "Partition : "
                                            + result
                                            .getRecordMetadata()
                                            .partition()
                            );
                            System.out.println(
                                    "Offset    : "
                                            + result
                                            .getRecordMetadata()
                                            .offset()
                            );
                            System.out.println(
                                    "========================================"
                            );
                        }
                    });

            System.out.println(
                    "Order Created : "
                            + order.orderId()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to serialize OrderEvent",
                    e
            );
        }

        return order;
    }

    public List<Order> findAll() {

        return List.copyOf(orders);
    }
}