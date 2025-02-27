package com.vayam.kafkacomsumer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vayam.kafkacomsumer.service.KafkaProducerService;

@Controller
public class KafkaMessageController {

    private final KafkaProducerService kafkaProducerService;

    public KafkaMessageController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @GetMapping("/")
    public String showForm() {
        return "index";
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam("message") String message, Model model) {
        kafkaProducerService.sendMessage("my-topic", message);
        model.addAttribute("msg", "Message Sent Successfully!");
        return "index";
    }
}
