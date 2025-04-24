package com.gairola.chat.controller;

import com.gairola.chat.model.ChatMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
;
import org.springframework.stereotype.Controller;


import java.security.Principal;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void send(@Payload ChatMessage message, Principal principal) {

        System.out.println("Sender: " + principal.getName()); // ✅ will work
        messagingTemplate.convertAndSendToUser(message.getTo(), "/queue/messages",
                message);
    }
}

