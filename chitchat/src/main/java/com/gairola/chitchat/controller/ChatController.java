package com.gairola.chitchat.controller;

import com.gairola.chitchat.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

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