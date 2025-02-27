package com.vayam.kafaka.service;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KafkaConsumerService {

    private final List<String> messages = new ArrayList<>();

    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void listen(ConsumerRecord<String, String> record) {
        messages.add(record.value());
        System.out.println("Received Message: " + record.value());
    }

    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }
}