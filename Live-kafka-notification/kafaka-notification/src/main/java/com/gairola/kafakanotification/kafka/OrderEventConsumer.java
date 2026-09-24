package com.gairola.kafakanotification.kafka;

import com.gairola.kafakanotification.model.OrderEvent;
import com.gairola.kafakanotification.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
public class OrderEventConsumer {

    private final NotificationService notificationService;
    private final JsonMapper jsonMapper;

    public OrderEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
        // Jackson 3 supports java.time natively without extra modules
        this.jsonMapper = JsonMapper.builder().build();
    }

    @KafkaListener(
            topics = "${app.kafka.order-topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(String message) {
        try {
            System.out.println("========================================");
            System.out.println("KAFKA MESSAGE RECEIVED");
            System.out.println("Raw JSON : " + message);

            OrderEvent event = jsonMapper.readValue(message, OrderEvent.class);

            System.out.println("Order ID : " + event.orderId());
            System.out.println("User ID  : " + event.userId());
            System.out.println("========================================");

            notificationService.createAndSend(
                    event.userId(),
                    event.orderId(),
                    event.eventType(),
                    event.message(),
                    event.createdAt()
            );
        } catch (Exception e) {
            System.err.println("ERROR processing Kafka message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}