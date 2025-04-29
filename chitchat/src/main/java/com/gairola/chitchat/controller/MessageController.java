package com.gairola.chitchat.controller;


import com.gairola.chitchat.model.UserLogin;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashSet;
import java.util.Set;

@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final Set<String> onlineUsers = new HashSet<>();

    public MessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/online")
    public void newUser(@Payload String username) {
        if (username != null && !username.trim().isEmpty()) {
            System.out.println("Received username: " + username);
            onlineUsers.add(username);
            messagingTemplate.convertAndSend("/topic/online", onlineUsers);
        } else {
            System.err.println("Empty username received!");
        }
    }

}
