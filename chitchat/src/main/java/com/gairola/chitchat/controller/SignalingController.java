package com.gairola.chitchat.controller;

import com.gairola.chitchat.model.SignalingMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignalingController {

    private final SimpMessagingTemplate messagingTemplate;

    public SignalingController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/call")
    public void call(SignalingMessage message) {
        messagingTemplate.convertAndSendToUser(message.getTo(), "/queue/call", message);
    }

    @MessageMapping("/endCall")
    public void endCall(SignalingMessage message) {
        messagingTemplate.convertAndSendToUser(message.getTo(), "/queue/end", message);
    }
}
