package com.vayam.kafaka.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vayam.kafaka.service.KafkaConsumerService;
import com.vayam.kafaka.service.KafkaProducerService;

@Controller
public class KafkaMessageController {

    private final KafkaProducerService kafkaProducerService;
    private final KafkaConsumerService kafkaConsumerService;

    public KafkaMessageController(KafkaProducerService kafkaProducerService, KafkaConsumerService kafkaConsumerService) {
        this.kafkaProducerService = kafkaProducerService;
        this.kafkaConsumerService = kafkaConsumerService;
    }

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("messages", kafkaConsumerService.getMessages());
        return "index";
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam("message") String message, Model model) {
        kafkaProducerService.sendMessage("my-topic", message);
        model.addAttribute("msg", "Message Sent Successfully!");
        model.addAttribute("messages", kafkaConsumerService.getMessages());
        return "index";
    }
}
