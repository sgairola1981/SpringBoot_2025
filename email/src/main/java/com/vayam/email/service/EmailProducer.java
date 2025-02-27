package com.vayam.email.service;
import com.vayam.email.request.EmailRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public EmailProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEmailRequest(EmailRequest emailRequest) {
        kafkaTemplate.send("email-topic", emailRequest.toJson());
    }
}
