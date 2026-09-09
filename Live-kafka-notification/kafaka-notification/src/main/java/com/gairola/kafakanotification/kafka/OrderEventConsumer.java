package com.gairola.kafakanotification.kafka;

import com.gairola.kafakanotification.model.OrderEvent;
import com.gairola.kafakanotification.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class OrderEventConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public OrderEventConsumer(
            NotificationService notificationService,
            ObjectMapper objectMapper) {

        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "${app.kafka.order-topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(String message) {

        try {

            System.out.println();
            System.out.println("========================================");
            System.out.println("KAFKA MESSAGE RECEIVED");
            System.out.println("Raw JSON : " + message);

            OrderEvent event =
                    objectMapper.readValue(
                            message,
                            OrderEvent.class
                    );

            System.out.println("Order ID : " + event.orderId());
            System.out.println("User ID  : " + event.userId());
            System.out.println("Type     : " + event.eventType());
            System.out.println("Message  : " + event.message());
            System.out.println("========================================");

            notificationService.createAndSend(
                    event.userId(),
                    event.orderId(),
                    event.eventType(),
                    event.message(),
                    event.createdAt()
            );

        } catch (Exception e) {

            System.err.println(
                    "ERROR processing Kafka message"
            );

            e.printStackTrace();
        }
    }
}